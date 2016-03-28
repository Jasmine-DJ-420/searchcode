package act;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class _es {
	
	private TransportClient client;
	private SearchHits shs;
	
	private long line_per_page = 10;   // 每页显示搜索结果个数
	private long max_line;  			  // 总行数
	private long max_page;			  // 总页数
	
	
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
			
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
			
			flag = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}  
		
		return flag;
	}
	
	
	// query
	public void search(String str){
		shs = null;
		
		QueryBuilder query = wildcardQuery("content", "*"+str+"*");
		
		SearchResponse response = client.prepareSearch("javacode_ast")
				.setTypes("github")
				       .setQuery(query)
				       .setSize(60)
				       .execute()
				       .actionGet();
		shs = response.getHits();
		
		
		
//		for(SearchHit hit : shs)
//		{
//			System.out.println("分数(score):"+hit.getScore()+", url:"+
//			hit.getSource().get("content"));
//		}
//		
	}
	
	// disconnect
	public void close(){
		client.close();
	}
	
	public static void main(String[] args) throws IOException{
		
		_es es = new _es();
		es.connection();
		System.out.println("ready!");
		es.search("");
		System.out.println(es.getTotalPage());
		es.getData(2);
		es.close();
	}
	
	public void insertDoc() throws IOException{
	}
	
	// max_page
	public long getTotalPage(){
		max_line = shs.totalHits();
		
		max_page = max_line/line_per_page + (long)((max_line%line_per_page==0)?0:1);
		
		return max_page;
	}
	
	public long get_max_page(){
		return max_page;
	}
	
	public SearchHit[] getData(int cur_page){
		SearchHit[] hits = new SearchHit[(int) line_per_page];
		
		long drift = line_per_page * (cur_page-1);
		
		for(int i=0;i<line_per_page;i++){
			hits[i] = shs.getAt((int) (drift+i));
		}
		
		return hits;
	}
	
	public SearchHit[] getHit(){
		return shs.getHits();
	}
}
