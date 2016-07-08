package act;

public class _content{

	private String content="";
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String code_content){
		this.content = code_content;
	}
	
	public String[] splitContent(){
		content = content.replace("'", "\\'");
		content = content.replace(" ", "\u00a0");
		String[] str = content.split("\n");
		return str;
	}
	
}