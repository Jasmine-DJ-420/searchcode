package act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XpathXML {
	DocumentBuilder builder;
	Document document;
	XPath xpath;
	
	
	public XpathXML(File file){
		try {
			builder = DocumentBuilderFactory.newInstance()
				    .newDocumentBuilder();
			document = builder.parse(file);
			xpath = XPathFactory.newInstance().newXPath();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String>[] get_file(int classid,int total){
		
		ArrayList<String>[] files_info = new ArrayList[total];
		
		try {
			NodeList clone_classes = (NodeList) xpath.evaluate("/clones/class[@classid="+classid+"]/source", document,
					    XPathConstants.NODESET);
			for(int i=0;i<clone_classes.getLength();i++){
				ArrayList<String> file_info = new ArrayList<String>();
				Node clone_class = clone_classes.item(i);
				String file = (String) xpath.evaluate("@file", clone_class,
					     XPathConstants.STRING);
				String starline = (String) xpath.evaluate("@startline", clone_class,
					     XPathConstants.STRING);
				String endline = (String) xpath.evaluate("@endline", clone_class,
					     XPathConstants.STRING);
				file_info.add(file);
				file_info.add(starline);
				file_info.add(endline);
				files_info[i] = file_info;
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return files_info;
	}

	public static void main(String[] args) {
		
		
	}

}
