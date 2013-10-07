/*******************************************************************************
 * Copyright 2013 Marco Balduini, Emanuele Della Valle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Acknowledgements:
 * 
 * This work was partially supported by the European project LarKC (FP7-215535) 
 * and by the European project MODAClouds (FP7-318484)
 ******************************************************************************/
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
