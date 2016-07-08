package act;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.elasticsearch.search.SearchHit;

@Path("/v1")
public class _query {
	
	private String index;
	private String type;
	private _es es;

	@GET
	@Path("/{index}/{type}/_search")
	@Produces(MediaType.APPLICATION_XML)
    public List<SearchHitResult> sayHello(@PathParam("index") String index,
    					   @PathParam("type") String type,
    					   @QueryParam("query") String query,
    					   @DefaultValue("null") @QueryParam("def") String definition
    					   ) {
        this.index = index;
        this.type = type;
        
        List<SearchHitResult> results = new ArrayList<SearchHitResult>();
        SearchHitResult result;

        es = new _es();
        es.connection();
        if(definition.equals("null")){
        	es.search(query);
        }
        else{
        	es.search(query, definition, 1);
        }
        
        SearchHit[] hits = es.getHit();
        es.close();
        
        for(SearchHit hit: hits){
        	result = new SearchHitResult(hit);
        	results.add(result);
        }
        
        
        return results;

    }
	
	@GET
	@Path("/{index}/{type}/_clone_search")
	@Produces(MediaType.APPLICATION_XML)
	public List<SearchGroupResult> GroupHit(@PathParam("index") String index,
			   @PathParam("type") String type,
			   @QueryParam("query") String query,
			   @DefaultValue("null") @QueryParam("def") String definition
			   ){
		List<SearchGroupResult> results = new ArrayList<SearchGroupResult>();
		SearchGroupResult result;
		
		CodeClone clone = new CodeClone();
		
		clone.init(query);
		
		SaxXML sax = clone.get_sax();
		
		sax.get_page(1);
		ArrayList<SearchHit2>[] shs = sax.getsh2();
		ArrayList<ClassInfor> info = sax.getCloneInfo();
		
		for(int i=0;i<shs.length;i++){
			result = new SearchGroupResult(shs[i],info.get(i).GetClassid(),info.get(i).GetNclones());
			results.add(result);
		}
		
		return results;
	}
}
