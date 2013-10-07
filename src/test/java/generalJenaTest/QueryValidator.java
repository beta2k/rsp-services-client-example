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
package generalJenaTest;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;

public class QueryValidator {

	public static void main(String[] args) {
		
		String querytext = 	"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX mc: <http://ex.org> " +
				"PREFIX mr: <http://ex.org/MonitoringRule#> " +
				"CONSTRUCT { [] rdf:type mc:ViolationEvent ;" +
				"mc:isGeneratedBy mr:ResponseTimeViolation . }" +
				"WHERE { " +
					"SELECT ?machine (AVG(?value) AS ?aggregation) " +
					"WHERE { " +
						"?datum mc:hasMonitoredMetric ?metric . " +
						"?datum mc:isAbout ?resource . " +
						"?datum mc:hasValue ?value . " +
						"?datum mc:isProcessedBy ?machine . " + 
					"} " +
					"GROUP BY (?machine)" +
					"HAVING (?aggregation > \"3000\"^^xsd:integer) " +
				"}";
		
		Query q = QueryFactory.create(querytext, Syntax.syntaxSPARQL_11);

	}

}
