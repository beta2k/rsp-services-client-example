package polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization;

import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities.Sparql_json_results;
import polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities.Variable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Sparql_json_deserialization {

	public static void main(String[] args) {

		JsonReader reader = null;
		Gson gson = new Gson();

		reader = new JsonReader(new StringReader("{\"head\": {\"vars\": [ \"provider\" , \"infrastrucureClass\" , \"infrastructure\" , \"platformClass\" , \"platform\" , \"softwareClass\" , \"software\" , \"operationClass\" , \"operation\" , \"metric\" , \"value\" , \"timestamp\" ]} ,\"results\": {\"bindings\": [{\"platform\": { \"type\": \"uri\" , \"value\": \"http://ec2-amazon-url.com:8080/MySQL#1\" } ,\"metric\": { \"type\": \"uri\" , \"value\": \"http://www.modaclouds.eu/ontology#AbortedConnections\" } ,\"value\": { \"datatype\": \"http://www.w3.org/2001/XMLSchema#integer\" , \"type\": \"typed-literal\" , \"value\": \"4\" } ,\"timestamp\": { \"datatype\": \"http://www.w3.org/2001/XMLSchema#integer\" , \"type\": \"typed-literal\" , \"value\": \"1379771981043\" }},{\"platform\": { \"type\": \"uri\" , \"value\": \"http://ec2-amazon-url.com:8080/MySQL#2\" } ,\"metric\": { \"type\": \"uri\" , \"value\": \"http://www.modaclouds.eu/ontology#AbortedConnections\" } ,\"value\": { \"datatype\": \"http://www.w3.org/2001/XMLSchema#integer\" , \"type\": \"typed-literal\" , \"value\": \"10\" } ,\"timestamp\": { \"datatype\": \"http://www.w3.org/2001/XMLSchema#integer\" , \"type\": \"typed-literal\" , \"value\": \"1379771981043\" }}]}}"));

		Sparql_json_results s = gson.fromJson(reader, Sparql_json_results.class);

		for(Map<String, Variable> m : s.getResults().getBindings()){
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
