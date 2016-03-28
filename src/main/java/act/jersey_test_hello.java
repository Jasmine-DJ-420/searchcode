package act;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
 
@Path("/hello")
public class jersey_test_hello {
 
    @GET
    public String sayHelloWorld() {
        return "Hello world";
    }   
 
    @GET
    @Path("/{name}")
    public String sayHello(@PathParam("name") String name) {
        return "Hello, " + name;
    }
    
//    @GET
//    @Path("/{name: [a-zA-Z][a-zA-Z0-9]*}")
//    public String sayHello2(@PathParam("name") String name) {
//        return "Hello, " + name;
//    }
//    
//    @GET
//    @Path("/name")
//    public String sayHello(
//          @PathParam("name") String name,
//          @QueryParam("count") @DefaultValue("1") int count)
//    {
//        StringBuffer sb = new StringBuffer();
//        for(int i=0; i<count; i++)
//        {
//            sb.append("Hello, " + name + "\n");
//        }
//        return sb.toString();
//    }
}