package act;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CodeClone {
	
	String exact_path = "G:/cygwin64/home/nicad_results/resources_";
	String resource;
	String result_path = "/home/nicad_results";
	String command = "cd /home/nicad_results ; nicad4 functions java resources_";
	int thread = 8;
	String search_str = "";
	SaxXML sax;
	String[] content;

	public void BashRun() {

		Runtime run = Runtime.getRuntime();

		try {
			String[] env = new String[] { "PATH=G:\\cygwin64\\bin\\" };
			Process proc = run.exec(new String[] { "G:\\cygwin64\\bin\\bash.exe", "-c", this.command + resource},
					env);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			while (br.ready())
				System.out.println(br.readLine());
		} catch (Exception e) {
			System.out.println("failed code clone search " + search_str + "!");
		}
	}
	
	public void WriteJavaFile(String str,long total_page){
		
		File file = new File(exact_path+"1");
		Thread delThread = null;
		
		if(file.isDirectory()){
			if(file.listFiles().length <= 0){
				// resources_1 is empty
				resource = "1";
			}
			else{
				resource = "2";
			}
			DelThread del = new DelThread(resource);
			delThread = new Thread(del);
			delThread.start();
		}
		
		search_str = str;
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		for(int i=0;i<thread;i++){
			executor.submit(new WriteTunnel(i,str,total_page,resource));
		}
		
		
		executor.shutdown();
		
		while(true){
			if(executor.isTerminated()){
				// code clone
				BashRun();
				break;
			}
		}	
		
		try {
			delThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		executor.shutdown();
	}
	
	public void AnalyzeXML(int page){
		
	}
	
	public void init(String str){
		
		long total_page;
		_es es = new _es();
		es.connection();
		
		total_page = es.CodeClone(str);
		
		WriteJavaFile(str,total_page);
		es.close();
		
		sax = new SaxXML();
		
		String path = exact_path + resource + "_functions-clones/"+"resources_"+resource+"_functions-clones-0.30-classes.xml";	 	
//		String path = "/Users/apple/Desktop/NiCad-4.0/examples/JHotDraw54b1_functions-clones/JHotDraw54b1_functions-clones-0.30-classes.xml";
		File file = new File(path);
		
		sax.parserXML(file);
	}
	
	// test
	public void init(){
		sax = new SaxXML();
		
		String path = "G:/cygwin64/home/nicad_results/resources_1_functions-clones/resources_1_functions-clones-0.30-classes.xml";	 	
//		String path = "/Users/apple/Desktop/NiCad-4.0/examples/JHotDraw54b1_functions-clones/JHotDraw54b1_functions-clones-0.30-classes.xml";
		File file = new File(path);
		
		sax.parserXML(file);
	}
	
	public SaxXML get_sax(){
		return this.sax;
	}
	
	public void setcontent(String[] str){
		this.content = str;
	}
	
	public String[] get_content(){
		return this.content;
	}

	
}

class DelThread implements Runnable{
	
	String dir_name;
	String exact_path = "G:/cygwin64/home/nicad_results/resources_";
	
	DelThread(String dir){
		if(dir.equals("1")){
			dir_name = exact_path + "2";
		}
		else{
			dir_name = exact_path + "1";
		}
	}
	
	public void run(){
		File dir = new File(dir_name);
		
		if (dir.isDirectory()) {
			deleteDir(dir);
		}
		
	}
	
	private static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i=0; i<children.length; i++) {
                children[i].delete();
            }
        }
    }
}  
