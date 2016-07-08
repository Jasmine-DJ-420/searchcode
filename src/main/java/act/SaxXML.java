package act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxXML {
	private int nclasses;
	private int classes_perpage;
	private int total_page;
	private _es es;
	private ArrayList<ClassInfor> CloneInfo;
	private int[] nclones;
	private XpathXML xpath;
	private ArrayList<SearchHit2>[] shs;
	
	public SaxXML(){
		this.classes_perpage = 10;
		es = new _es();
	}
	
	public ArrayList<ClassInfor> getCloneInfo(){
		return this.CloneInfo;
	}
	
	public ArrayList<SearchHit2>[] getsh2(){
		return shs;
	}
	
	public int get_number(){
		return CloneInfo.size();
	}
	
	public int[] get_page(int page){
		es.connection();
		ClassInfor info;
		nclones = new int[classes_perpage];   ////????? total_page compare to perpage
		shs = new ArrayList[classes_perpage];
		ArrayList<SearchHit2> sh_list;
		SearchHit2 sh2;
		for(int i=(page-1)*classes_perpage;i<classes_perpage;i++){
			sh_list = new ArrayList<SearchHit2>();
			info = CloneInfo.get(i);
			nclones[i] = info.GetNclones();
			
			ArrayList<String>[] xpath_files = xpath.get_file(info.GetClassid(),info.GetNclones());
			
			
			for(ArrayList<String> xpath_file: xpath_files){
				sh2 = new SearchHit2();
				sh2.setSL(Integer.parseInt(xpath_file.get(1)));
				sh2.setEL(Integer.parseInt(xpath_file.get(2)));
				sh2.setSH(es.clone_search(xpath_file.get(0).split("/")[1]));
				sh_list.add(sh2);
			}
			
			shs[i] = sh_list;
		}
		es.close();
		
		return nclones;
	}
	
	public void parserXML(File file){
		SAXParserFactory saxfac = SAXParserFactory.newInstance();
		xpath = new XpathXML(file);
		
		try {
            SAXParser saxparser = saxfac.newSAXParser();
            InputStream is = new FileInputStream(file);
            saxparser.parse(is, new MySAXHandler());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void totalpage(){
		this.total_page = nclasses/classes_perpage + ((nclasses%classes_perpage==0)?0:1);;
	}
	
	public int get_totalpage(){
		return this.total_page;
	}
	
	public int get_nclasses(){
		return this.nclasses;
	}
	
	public int[] get_nclones(){
		return this.nclones;
	}

	
	class MySAXHandler extends DefaultHandler {
	    
	    public void startDocument() throws SAXException {
//	         System.out.println("Start XML Document!");
	    	
	    }

	    public void endDocument() throws SAXException {
//	         System.out.println("Finish XML Document!");
	    	
	    	// Sort 'cause clone classes' numbers
	    	Collections.sort(CloneInfo);
	    }

	    public void startElement(String uri, String localName, String qName,
	            Attributes attributes) throws SAXException {
	    	if(qName.equals("classinfo")){
	    		nclasses = Integer.parseInt(attributes.getValue("nclasses"));
	    		totalpage();
	    		CloneInfo = new ArrayList<ClassInfor>();
	    		
	    	}
	    	else if(qName.equals("class")){
	    		int classid = Integer.parseInt(attributes.getValue("classid"));
	    		int nclones = Integer.parseInt(attributes.getValue("nclones"));
	    		ClassInfor classinfo = new ClassInfor(classid,nclones);
	    		CloneInfo.add(classinfo);
	    	}
	    }

	    public void endElement(String uri, String localName, String qName)
	            throws SAXException {
	    }

	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
//	    	String content = new String(ch, start, length);
	    }
	}
	
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		String path_mac = "/Users/apple/Desktop/NiCad-4.0/examples/JHotDraw54b1_functions-clones/JHotDraw54b1_functions-clones-0.30-classes.xml";
		String path_win = "G:/cygwin64/home/nicad_results/resources_2_functions-clones/resources_2_functions-clones-0.30-classes.xml";
		File file = new File(path_win);
		SaxXML sax = new SaxXML();
		sax.parserXML(file);
		sax.get_page(1);
		
		ArrayList<SearchHit2>[] temp = sax.getsh2();
		
		for(ArrayList<SearchHit2> al : temp ){
			System.out.println("class");
			for(SearchHit2 sh: al){
				System.out.println(sh.getSL());
			}
		}
		
	}
	
}
