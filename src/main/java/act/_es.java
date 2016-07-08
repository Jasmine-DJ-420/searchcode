package act;

import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class _es {
	
	private TransportClient client;
	private SearchHits shs;
	
	private long line_per_page = 10;   
	private long max_line;  			  
	private long max_page;			  
	
	private long class_search ;
	private long import_search ;
	private long method_search;
	private long comment_search ;
	private long extend_search ;
	private long implement_search ;
	
	// connect
	public boolean connection(){
		boolean flag = false;
		
		Settings settings = Settings.builder()
				.put("client.transport.sniff", true) 
				.put("cluster.name", "sdp")
				.build();  
		client = new TransportClient.Builder().settings(settings).build();
		try {
//			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.168"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.142"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.222"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.105"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.223"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.224"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.234"), 9300))
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.237"), 9300))
//			;
			
//			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
			
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.105"),9300))
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.142"),9300))
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.222"),9300))
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.223"),9300))
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.237"),9300));
			
			flag = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}  
		
		System.out.println("es attached");
		
		return flag;
	}
	
	
	// query
	public long search(String str, String source_name, int page){
		SearchHits shs_temp;
		int from = 10*(page-1);
		if(page==0)
			from = 1;
		
		String[] def_str = source_name.split(",");
		QueryBuilder query;
		
		if(def_str.length==1){
			query = wildcardQuery(source_name, "*"+str+"*");
		}
		else{
			query = QueryBuilders.boolQuery();
			for(int i=0;i<def_str.length;i++){
				((BoolQueryBuilder) query).should(QueryBuilders.termQuery(def_str[i], str));
			}
		}
		
		SearchResponse response = client.prepareSearch("javacode_ast")
				.setTypes("github")
				       .setQuery(query)
				       .setSize(10)
				       .setFrom(from)
				       .execute()
				       .actionGet();
		
		shs_temp = response.getHits();
		System.out.println(shs_temp.getTotalHits());
		
		if(page!=0){
			shs = shs_temp;
		}
		
		return shs_temp.getTotalHits();
		
		
//		for(SearchHit hit : shs)
//		{
//			System.out.println("(score):"+hit.getScore()+", url:"+
//			hit.getSource().get("content"));
//		}
//		
	}
	
	public long search(String str){
		shs = null;
		
		QueryBuilder query = wildcardQuery("content", "*"+str+"*");
		
		SearchResponse response = client.prepareSearch("javacode_ast")
				.setTypes("github")
				       .setQuery(query)
				       .setSize(10)
				       .execute()
				       .actionGet();
		shs = response.getHits();
		System.out.println(shs.getTotalHits());	
		
		class_search = search(str, "class", 0);
		import_search = search(str, "import", 0);
		method_search = search(str, "method", 0);
		comment_search = search(str, "comment", 0);
		extend_search = search(str, "extend", 0);
		implement_search = search(str, "implement", 0);
		return shs.getTotalHits();
		
	}
	
	public Map<String,Object> clone_search(String file_name){
//		shs = null;
//		QueryBuilder query = wildcardQuery("fileName", file_name);
//		SearchResponse response = client.prepareSearch("javacode_ast")
//				.setTypes("github")
//				       .setQuery(query)
//				       .setSize(10)
//				       .execute()
//				       .actionGet();
//		shs = response.getHits();
//		
//		return shs.getAt(0);
		
		GetResponse response = client.prepareGet("javacode_ast","github",file_name.split(".java")[0]).execute().actionGet();

		
		return response.getSource();
	}
	
	// disconnect
	public void close(){
		if(client!=null){
			client.close();			
		}
		System.out.println("es closed");
	}
	
	public TransportClient clTrans(){
		return client;
	}
	
	public static void main(String[] args) throws IOException{
		
		_es es = new _es();
		es.connection();
		System.out.println("ready!");
//		es.search("write","method",1);
////		es.search("write");
//		es.getTotalPage();
////		System.out.println(es.get_max_page());
//		for(SearchHit hit : es.shs)
//		{
//			System.out.println(hit.getSource());
//		}
		
//		es.CodeClone("write");
		es.search("write");
		SearchHit[] hits = es.getData();
		
		System.out.println(es.clone_search("AVPGYYHM6QRg_fbfsxjE"));
		
		es.close();
	}
	
	
	public long get_lines(){
		return shs.totalHits();
	}
	
	// max_page
	public void getTotalPage(){
		max_line = shs.totalHits();
		
		max_page = max_line/line_per_page + (long)((max_line%line_per_page==0)?0:1);
	}
	
	public long get_max_page(){
		return max_page;
	}
	
	public SearchHit[] getData(int cur_page){
		
		
		long drift = line_per_page * (cur_page-1);
		
		long temp = line_per_page;
		if (cur_page==max_page){
			temp = max_line%line_per_page;
			if (temp == 0){
				temp = line_per_page;
			}
		}
		else if(cur_page>max_page){
			temp = 0;
		}
		SearchHit[] hits = new SearchHit[(int) temp];
		
		for(int i=0;i<temp;i++){
			hits[i] = shs.getAt((int) (drift+i));
		}
		
		return hits;
	}
	
	public SearchHit[] getData(){
		int temp = shs.getHits().length;
		SearchHit[] hits = new SearchHit[temp];
		
		for(int i=0;i<temp;i++){
			hits[i] = shs.getAt(i);
		}
		
		return hits;
	}

	public long CodeClone(String str){
		
		QueryBuilder query = wildcardQuery("content", "*"+str+"*");
		
		SearchResponse response = client.prepareSearch("javacode_ast")
				.setTypes("github")
				       .setQuery(query)
				       .setSize(10)
				       .execute()
				       .actionGet();
		SearchHits searchhits = response.getHits();
		System.out.println(searchhits.getTotalHits());	
		long total_num = searchhits.getTotalHits();
		long total_page = total_num/line_per_page + (long)((total_num%line_per_page==0)?0:1);
		
		return total_page;
	}

	public SearchHit[] getHit(){
		return shs.getHits();
	}
	
	public long getMe(){
		return this.method_search;
	}
	
	public long getIm(){
		return this.import_search;
	}
	
	public long getComm(){
		return this.comment_search;
	}
	
	public long getImpl(){
		return this.implement_search;
	}
	
	public long getExt(){
		return this.extend_search;
	}
	
	public long getCla(){
		return this.class_search;
	}
}
