package act;

import java.util.Arrays;
import java.util.Map;


public class SearchHit2 {
	private Map<String,Object> sh;
	int starline;
	int endline;
	
	public void setSH(Map<String,Object> sh){
		this.sh = sh;
	}
	
	public void setSL(int startline){
		this.starline = startline;
	}
	
	public void setEL(int endline){
		this.endline = endline;
	}
	
	public int getSL(){
		return this.starline;
	}
	
	public int getEL(){
		return this.endline;
	}
	
	
	public Map<String,Object> getSH(){
		return this.sh;
	}
	
	public String[] getContent(){
		String content;
		String[] content_result;
		
		content = (String) sh.get("content");
		
		content = content.replace("'", "\\'");
		content = content.replace(" ", "\u00a0");
		String[] str = content.split("\n");
		
		content_result = Arrays.copyOfRange(str, starline, endline+1);
		
		return content_result;
	}
	
	public String[] getAPIcontent(){
		String content;
		String[] content_result;
		
		content = (String) sh.get("content");
		
		String[] str = content.split("\n");
		
		content_result = Arrays.copyOfRange(str, starline, endline+1);
		
		return content_result;
	}
}
