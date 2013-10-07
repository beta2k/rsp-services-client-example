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

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.annotations.Expose;

public class Result {
	
	@Expose private ArrayList<Map<String, Variable>> bindings;

	public ArrayList<Map<String, Variable>> getBindings() {
		return bindings;
	}

	public void setBindings(ArrayList<Map<String, Variable>> bindings) {
		this.bindings = bindings;
	}
	
}
