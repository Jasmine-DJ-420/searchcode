package act;

public class ClassInfor implements Comparable<ClassInfor>{
	private int classid;
	private int nclones;
	
	public ClassInfor(int classid, int nclones){
		this.classid = classid;
		this.nclones = nclones;
	}
	
	public void SetNclones(int nclones){
		this.nclones = nclones;			
	}
	
	public void SetClassid(int classid){
		this.classid = classid;
	}
	
	public Integer GetNclones(){
		return this.nclones;
	}
	
	public Integer GetClassid(){
		return this.classid;
	}
	
	public int compareTo(ClassInfor arg0) { 
        return arg0.GetNclones().compareTo(this.GetNclones()); 
    }
}
