package act;

import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class WriteTunnel implements Callable<Integer>{
	
	private int thread_num;
	private String search_str;
	
	private long total_page;
	private TransportClient client;
	
	private int per_page = 60;
	
	String result_path = "G:/cygwin64/home/nicad_results/resources_";
	
	public WriteTunnel(int num, String search_str,long total_page,String resources_num){
		this.thread_num = num;
		this.search_str = search_str;
		this.total_page = total_page;
		this.result_path = this.result_path + resources_num + "/";
	}
	
	@Override
	public Integer call() throws Exception {
		
		connection();
		WriteJava();
		close();
		return null;
		
	}
	
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
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.222"),9300))
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.223"),9300));
			
			flag = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}  
		
		return flag;
	}
	
	public void WriteJava(){

		int thread_page;
		String content;
		String file_name;
		for(int i=0;i*5+thread_num<total_page;i++){
			
			thread_page = i*5 + thread_num;
			int from = per_page*thread_page ;
			
			QueryBuilder query = wildcardQuery("content", "*"+search_str+"*");
			SearchResponse response = client.prepareSearch("javacode_ast")
					.setTypes("github")
					       .setQuery(query)
					       .setSize(per_page)
					       .setFrom(from)
					       .execute()
					       .actionGet();
			SearchHits shs = response.getHits();
			
			for(SearchHit hit : shs)
			{
				content = (String) hit.getSource().get("content");
//				file_name = (String) hit.getSource().get("id");
				file_name = hit.getId()+".java";
				
				WriteFile(content,file_name);
			}
		}
	}
	
	private void WriteFile(String content, String name){
		try {
			File file = new File(result_path+name);
			
			if (!file.exists()) {
			    file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		if(client!=null){
			client.close();			
		}
		System.out.println("es closed");
	}
	
	

}
