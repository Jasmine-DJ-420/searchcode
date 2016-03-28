package act;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.elasticsearch.search.SearchHit;

@Path("/v1")
public class _query {
	
	private String index;
	private String type;
	private _es es;

	@GET
	@Path("/{index}/{type}/_search")
    public String sayHello(@PathParam("index") String index,
    					   @PathParam("type") String type,
    					   @QueryParam("q") String query
    					   ) {
        this.index = index;
        this.type = type;

        es = new _es();
        es.connection();
        es.search(query);
        SearchHit[] hits = es.getHit();
        es.close();
        
        String return_result="{" + hits[0].sourceAsString();
        for(int i=1;i<hits.length;i++){
        	return_result += "," + hits[i].sourceAsString();
        }
        return_result += "}";
        

        return return_result;
    }
}
