package polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.annotations.Expose;

public class List_of_Sparql_json_results_oracle {

	@Expose private List<Sparql_json_results_oracle> relations;
	private long startTS;

	public long getStartTS() {
		return startTS;
	}

	public void setStartTS(long startTS) {
		this.startTS = startTS;
	}

	public List<Sparql_json_results_oracle> getRelations() {
		return relations;
	}

	public void setRelations(List<Sparql_json_results_oracle> relations) {
		this.relations = relations;
	}

	public void add(Sparql_json_results_oracle element){
		if(relations == null){
			relations = new ArrayList<Sparql_json_results_oracle>();
		}
		relations.add(element);
	}
	
	public int size(){
		if(relations == null){
			relations = new ArrayList<Sparql_json_results_oracle>();
		}
		return relations.size();
	}
	
	public Sparql_json_results_oracle getElement(int index){
		if(relations == null){
			relations = new ArrayList<Sparql_json_results_oracle>();
		}
		return relations.get(index);
	}

	public void printSerializationOnConsole() {

		for(Sparql_json_results_oracle element : relations){

			System.out.println(element.getTimestamp());
			for(Map<String, Variable> m : element.getResults().getBindings()){
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

}
