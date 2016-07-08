package act;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.elasticsearch.search.SearchHit;

@XmlRootElement
public class SearchHitResult {
	
	private String userName;
	private String fileName;
	private String projectName;
	private String file_url;
	private String project_url;
	private String content;
	private String extend;
	private String class_names;
	private String method_names;
	private String import_names;
	private String comment;
	private String implement;
	private String startline;
	private String endline;

	public SearchHitResult(){};
	
	public SearchHitResult(SearchHit sh){
		Map<String, Object> sh_map = sh.getSource();
		try {
			this.userName = sh_map.get("userName").toString();
			this.fileName = sh_map.get("fileName").toString();
			this.projectName = sh_map.get("projectName").toString();
			this.file_url = sh_map.get("file_url").toString();
			this.project_url = sh_map.get("project_url").toString();
			this.content = sh_map.get("content").toString();
			this.extend = sh_map.get("extend").toString();
			this.class_names = sh_map.get("class").toString();
			this.method_names = sh_map.get("method").toString();
			this.import_names = sh_map.get("import").toString();
			this.comment = sh_map.get("comment").toString();
			this.implement = sh_map.get("implement").toString();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("null pointer exception");
		}
	}
	
	public SearchHitResult(SearchHit2 sh){
		Map<String, Object> sh_map = sh.getSH();
		try {
			this.userName = sh_map.get("userName").toString();
			this.fileName = sh_map.get("fileName").toString();
			this.projectName = sh_map.get("projectName").toString();
			this.file_url = sh_map.get("file_url").toString();
			this.project_url = sh_map.get("project_url").toString();
			this.extend = sh_map.get("extend").toString();
			this.class_names = sh_map.get("class").toString();
			this.method_names = sh_map.get("method").toString();
			this.import_names = sh_map.get("import").toString();
			this.comment = sh_map.get("comment").toString();
			this.implement = sh_map.get("implement").toString();
		} catch (NullPointerException e) {
			System.out.println("null pointer exception");
		}
		
		this.startline = Integer.toString(sh.getSL());
		this.endline = Integer.toString(sh.getEL());
		String[] content_temp = sh.getAPIcontent();
		String temp="";
		for(int i=0;i<content_temp.length;i++){
			temp = temp + content_temp[i];
		}
		this.content = temp;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getProjectName(){
		return this.projectName;
	}
	
	public void setProjectName(String projectName){
		this.projectName = projectName;
	}
	
	public String getFile_url(){
		return this.file_url;
	}
	
	public void setFile_url(String file_url){
		this.file_url = file_url;
	}
	
	public String getProject_url(){
		return this.project_url;
	}
	
	public void setProject_url(String project_url){
		this.project_url = project_url;
	}
	
	public String getContent(){
		return this.content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getExtend(){
		return this.extend;
	}
	
	public void setExtend(String extend){
		this.extend = extend;
	}
	
	public String getClass_names(){
		return this.class_names;
	}
	
	public void setClass_names(String class_names){	
		this.class_names = class_names;
	}
	
	public String getMethod_names(){
		return this.method_names;
	}
	
	public void setMethod_names(String method_names){
		this.method_names = method_names;
	}
	
	public String getImport_names(){
		return this.import_names;
	}
	
	public void setImport_names(String import_names){
		this.import_names = import_names;
	}
	
	public String getComment(){
		return this.comment;
	}
	
	public void setcomment(String comment){
		this.comment = comment;
	}
	
	public String getImplement(){
		return this.implement;
	}
	
	public void setImplement(String implement){
		this.implement = implement;
	}
	
}
