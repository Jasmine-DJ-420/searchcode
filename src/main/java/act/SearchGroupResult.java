package act;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchGroupResult {
	private ArrayList<SearchHitResult> sh;
	private int class_id;
	private int hits_number;
	
	public SearchGroupResult(){};
	
	public SearchGroupResult(ArrayList<SearchHit2> sh2,int id, int num){
		this.sh = new ArrayList<SearchHitResult>();
		this.class_id = id;
		this.hits_number = num;
		
		for(SearchHit2 sh: sh2){
			SearchHitResult shr = new SearchHitResult(sh);
			this.sh.add(shr);
		}
	}
	
	public int getClass_id(){
		return this.class_id;
	}
	
	public void setClass_id(int class_id){
		this.class_id = class_id;
	}
	
	public int getHits_number(){
		return this.hits_number;
	}
	
	public void setHits_number(int hits_number){
		this.hits_number = hits_number;
	}
	
	public ArrayList<SearchHitResult> getSh(){
		return this.sh;
	}
	
	public void setSh(ArrayList<SearchHitResult> sh){
		this.sh = sh;
	}
}
