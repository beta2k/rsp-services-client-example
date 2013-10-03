package polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.annotations.Expose;

public class Sparql_json_results_oracle {

	@Expose private Head head;
	@Expose private long timestamp;
	@Expose private Result results;

	public Head getHead() {
		return head;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public Result getResults() {
		return results;
	}
	public void setResults(Result results) {
		this.results = results;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void printSerializationOnConsole() {
		
		System.out.println(timestamp);
		for(Map<String, Variable> m : results.getBindings()){
			Set<Entry<String, Variable>> set = m.entrySet();
			for(Entry<String, Variable> e : set){
				System.out.println(e.getValue().getType());
				if(e.getValue().getType().equals("typed-literal"))
					System.out.println(e.getValue().getDatatype());
				System.out.println(e.getValue().getValue());
				System.out.println();
			}
		}

	}

}
