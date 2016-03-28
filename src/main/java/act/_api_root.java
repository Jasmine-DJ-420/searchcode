package act;


import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
 
@ApplicationPath("api")
public class _api_root extends ResourceConfig{
    public _api_root(){
        packages("act");
    }
}
