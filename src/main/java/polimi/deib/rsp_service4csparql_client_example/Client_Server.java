/*******************************************************************************
 * Copyright 2013 DEIB - Politecnico di Milano
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
 ******************************************************************************/
package polimi.deib.rsp_service4csparql_client_example;

import java.util.Hashtable;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.rsp_service4csparql_client_example.configuration.Config;
import polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities.List_of_Sparql_json_results_oracle;
import polimi.deib.rsp_service4csparql_client_example.results_manipulator.ResultsManipulator_Oracle;
import polimi.deib.rsp_service4csparql_client_example.streamer.BaseStreamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.ISWC_DemoStreamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.Inference_Streamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.MultiThreadStreamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.Static_Knowledge_Test_Streamer;
import polimi.deib.timisoara_demo.ontology.MC;
import polimi.deib.timisoara_demo.streamer.CloudWatchCollectorStreamer;
import polimi.deib.timisoara_demo.streamer.MySQLAggregatedCollectorStreamer;
import polimi.deib.timisoara_demo.streamer.OFBizLogAggregatedCollectorStreamer;

public class Client_Server extends Application{

	private static Component component;
	private static Hashtable<String, String> queryProxyIdTable = new Hashtable<String, String>();
	private static Logger logger = LoggerFactory.getLogger(Client_Server.class.getName());

	private static int ID = 0;

	private static List_of_Sparql_json_results_oracle json_results_list = new List_of_Sparql_json_results_oracle();


	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception{

		//Set to false if you need client server only for writing results
		boolean startExample = true;

		final int SINGLE_SELECT_QUERY_SINGLE_OBSERVER = 0;
		final int SINGLE_CONSTRUCT_QUERY_SINGLE_OBSERVER = 1;
		final int QUERY_CHAIN = 2;
		final int MODACLOUDS_FIRST_DEMO = 3;
		final int SINGLE_CONSTRUCT_QUERY_SINGLE_OBSERVER_HI_PRESSURE_TEST = 4;
		final int SPARQL_UDATE = 5;
		final int RDFS_INFERENCE = 6;
		final int ISWC_TUTORIAL_DEMO_TRANSITIVE_INFERENCE = 7;
		final int TIMISOARA_DEMO = 8;

		int key = SINGLE_SELECT_QUERY_SINGLE_OBSERVER;
		String csparqlServerAddress = "http://localhost:8175";

		String actual_client_address;
		int actual_client_port;

		component = new Component();
		component.getServers().add(Protocol.HTTP, Config.getInstance().getServerPort());
		component.getClients().add(Protocol.FILE);  

		Client_Server csparqlServer = new Client_Server();
		component.getDefaultHost().attach("", csparqlServer);

		component.start();

		if(startExample){

			actual_client_address = component.getServers().get(0).getAddress();
			if(actual_client_address == null)
				actual_client_address = "http://localhost";
			actual_client_port = component.getServers().get(0).getActualPort();

			String query;
			String query2;

			String inputstreamName = null;
			String streamName;
			String streamName2;

			String queryURI;
			String queryURI2;

			String generalIRI = "http://ex.org/";

			Csparql_Remote_API csparqlAPI = new Csparql_Remote_API(csparqlServerAddress);

			switch(key){
			case SINGLE_SELECT_QUERY_SINGLE_OBSERVER:
				try {

					inputstreamName = generalIRI + "onlinefeeding";

					csparqlAPI.registerStream(inputstreamName);

					Thread.sleep(1000);


					query = "REGISTER QUERY HelloWorld AS " +
							"SELECT ?s ?p ?o " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 10s STEP 10s] " +
							"WHERE { ?s ?p ?o }";


					queryURI = csparqlAPI.registerQuery("HelloWorld", query);

					System.out.println(queryURI);
					
					json_results_list.setStartTS(System.currentTimeMillis());

					Client_Server.queryProxyIdTable.put(query, queryURI);

					String obsURI = csparqlAPI.addObserver(queryURI, actual_client_address + ":" + actual_client_port + "/results");
					System.out.println(obsURI);

					BaseStreamer bs = new BaseStreamer(csparqlAPI, inputstreamName, 2000, generalIRI);
					new Thread(bs).start(); 

					System.out.println(csparqlAPI.getStreamInfo(inputstreamName));
					System.out.println(csparqlAPI.getQueryInfo(queryURI));


					Thread.sleep(60000);

					System.out.println(csparqlAPI.deleteObserver(obsURI));
					System.out.println(csparqlAPI.unregisterQuery(queryURI));
					System.out.println(csparqlAPI.unregisterStream(inputstreamName));
					bs.stopStream();

				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				}

				break;

			case SINGLE_CONSTRUCT_QUERY_SINGLE_OBSERVER:

				try{


					inputstreamName = generalIRI + "onlinefeeding";

					csparqlAPI.registerStream(inputstreamName);

					Thread.sleep(1000);

					query = "REGISTER QUERY HelloWorld AS " +
							"CONSTRUCT { ?s ?p ?o } " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 5s STEP 5s] " +
							"WHERE { ?s ?p ?o }";

					queryURI = csparqlAPI.registerQuery("HelloWorld", query);
					json_results_list.setStartTS(System.currentTimeMillis());

					Client_Server.queryProxyIdTable.put(query, queryURI);

					String obsURI = csparqlAPI.addObserver("HelloWorld", actual_client_address + ":" + actual_client_port + "/results");

					BaseStreamer bs = new BaseStreamer(csparqlAPI, inputstreamName, 2000, generalIRI);
					new Thread(bs).start(); 

					System.out.println(csparqlAPI.getStreamInfo(inputstreamName));
					System.out.println(csparqlAPI.getQueryInfo(queryURI));


					Thread.sleep(60000);

					System.out.println(csparqlAPI.deleteObserver(obsURI));
					System.out.println(csparqlAPI.unregisterQuery(queryURI));
					System.out.println(csparqlAPI.unregisterStream(inputstreamName));
					bs.stopStream();
					
				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				}

				break;

			case QUERY_CHAIN:

				try{
					inputstreamName = generalIRI + "onlinefeeding";

					csparqlAPI.registerStream(inputstreamName);

					Thread.sleep(1000);

					query = "REGISTER STREAM HelloWorld AS " +
							"CONSTRUCT { ?s ?p ?o } " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 2s STEP 2s] " +
							"WHERE { ?s ?p ?o }";

					queryURI = csparqlAPI.registerQuery("HelloWorld", query);
					json_results_list.setStartTS(System.currentTimeMillis());


					if(!queryURI.equals(500)){

						streamName2 = "http://streamreasoning.org/" + queryURI;
						query2 = "REGISTER QUERY HelloWorld2 AS " +
								"SELECT ?s " +
								"FROM STREAM <" + streamName2 + "> [RANGE 10s STEP 10s] " +
								"WHERE { ?s ?p ?o }";

						queryURI2 = csparqlAPI.registerQuery("HelloWorld2", query2);
						json_results_list.setStartTS(System.currentTimeMillis());

						Client_Server.queryProxyIdTable.put(query, queryURI);
						Client_Server.queryProxyIdTable.put(query2, queryURI2);

						csparqlAPI.addObserver(queryURI2, actual_client_address + ":" + actual_client_port + "/results");

						new Thread(new BaseStreamer(csparqlAPI, inputstreamName, 2000, generalIRI)).start(); 

					} else {
						System.out.println("ERROR");
					}
				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				}

				break;

			case MODACLOUDS_FIRST_DEMO:

				try{
					inputstreamName = generalIRI + "onlinefeeding";

					csparqlAPI.registerStream(inputstreamName);

					Thread.sleep(1000);

					query = "REGISTER STREAM ResponseTimeViolation AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + generalIRI + "> " +
							"PREFIX mr: <" + generalIRI + "MonitoringRule#> " +
							"CONSTRUCT { [] rdf:type mc:ViolationEvent ; " +
							"mc:isGeneratedBy mr:ResponseTimeViolation ; " +
							"mc:on ?machine ; " +
							"mc:hasValue ?aggregation . }" +
							"FROM STREAM <" + inputstreamName + "> [RANGE 10s STEP 10s] " +
							"WHERE { { " +
							"SELECT ?machine (AVG(?value) AS ?aggregation) " +
							"WHERE { " +
							"?datum mc:hasMonitoredMetric ?metric . " +
							"?datum mc:isAbout ?resource . " +
							"?datum mc:hasValue ?value . " +
							"?datum mc:isProcessedBy ?machine . " + 
							"} " +
							"GROUP BY ?machine " +
							"HAVING (?aggregation > \"3000\"^^xsd:integer) " +
							"} }";

					queryURI = csparqlAPI.registerQuery("ResponseTimeViolation", query);
					json_results_list.setStartTS(System.currentTimeMillis());


					Client_Server.queryProxyIdTable.put(query, queryURI);

					csparqlAPI.addObserver(queryURI, actual_client_address + ":" + actual_client_port + "/results");

					new Thread(new ISWC_DemoStreamer(csparqlAPI, inputstreamName, 2000, generalIRI)).start(); 

				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				}

				break;

			case SINGLE_CONSTRUCT_QUERY_SINGLE_OBSERVER_HI_PRESSURE_TEST:

				try{


					inputstreamName = generalIRI + "onlinefeeding";

					csparqlAPI.registerStream(inputstreamName);

					Thread.sleep(1000);

					query = "REGISTER QUERY HelloWorld AS " +
							"CONSTRUCT { ?s ?p ?o } " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 2s STEP 2s] " +
							"WHERE { ?s ?p ?o }";

					queryURI = csparqlAPI.registerQuery("HelloWorld", query);
					json_results_list.setStartTS(System.currentTimeMillis());

					Client_Server.queryProxyIdTable.put(query, queryURI);

					csparqlAPI.addObserver("HelloWorld", actual_client_address + ":" + actual_client_port + "/results");

					//			new Thread(new BaseStreamer(csparqlAPI, inputstreamName, 0, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 
					new Thread(new MultiThreadStreamer(csparqlAPI, inputstreamName, generalIRI)).start(); 

				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				}

				break;

			case SPARQL_UDATE:

				try{

					String onlineStaticFileURL = "https://www.dropbox.com/s/2y01w93j6eujq0a/StaticKnowledgeTest.rdf?dl=1";

					inputstreamName = generalIRI + "onlinefeeding";
					csparqlAPI.registerStream(inputstreamName);

					query = "REGISTER QUERY UpdateTest AS " +
							"PREFIX ex:<http://streamreasoning.org#> " +
							"SELECT ?person1 ?firstRoom ?person2 ?secondRoom " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 5s STEP 5s] "	+ 
							"FROM <" + onlineStaticFileURL + "> " +
							"WHERE { " +
							"?person1 ex:isIn ?firstRoom . " +
							"?person2 ex:isIn ?secondRoom . " +
							"?firstRoom ex:contiguous ?secondRoom . " +
							"FILTER(?person2 != ?person1 && ?firstRoom != ?secondRoom) " +
							"}";

					queryURI = csparqlAPI.registerQuery("UpdateTest", query);
					json_results_list.setStartTS(System.currentTimeMillis());

					Client_Server.queryProxyIdTable.put(query, queryURI);

					csparqlAPI.addObserver("UpdateTest", actual_client_address + ":" + actual_client_port + "/results");

					new Thread(new Static_Knowledge_Test_Streamer(csparqlAPI, inputstreamName)).start();

					Thread.sleep(20000);

					String updateQuery = "PREFIX sr:<http://streamreasoning.org#> INSERT DATA { GRAPH <" + onlineStaticFileURL + "> { sr:r3  sr:contiguous  sr:r4 } }";
					csparqlAPI.launchUpdateQuery(updateQuery);

				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				}

				break;

			case RDFS_INFERENCE:

				try{

					String rdfSchemaURL = "https://www.dropbox.com/s/q5csfdhoifba2rl/rdfsDemoSchema.rdf?dl=1";

					inputstreamName = generalIRI + "onlinefeeding";
					csparqlAPI.registerStream(inputstreamName);

					//			query = "REGISTER QUERY InferenceTest AS " +
					//					"PREFIX ex:<http://streamreasoning.org#> " +
					//					//					"CONSTRUCT { ?s ?p ?o } " +
					//					"SELECT ?s ?p ?o " +
					//					"FROM STREAM <" + inputstreamName + "> [RANGE 10s STEP 10s] "	+ 
					//					"FROM <" + rdfSchemaURL + "> " +
					//					"WHERE { " +
					//					"?s ?p ?o " +
					//					"}";

					query = "REGISTER QUERY InferenceTest AS " +
							"PREFIX ex:<http://streamreasoning.org#> " +
							"CONSTRUCT { ?s ex:parent ex:m } " +
							//					"SELECT ?s " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 10s STEP 10s] "	+ 
							"FROM <" + rdfSchemaURL + "> " +
							"WHERE { " +
							"?s ex:parent ex:m " +
							"}";

					queryURI = csparqlAPI.registerQuery("InferenceTest", query);
					json_results_list.setStartTS(System.currentTimeMillis());

					Client_Server.queryProxyIdTable.put(query, queryURI);

					csparqlAPI.addObserver("InferenceTest", actual_client_address + ":" + actual_client_port + "/results");

					new Thread(new Inference_Streamer(csparqlAPI, inputstreamName)).start();

				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} 

				break;

			case ISWC_TUTORIAL_DEMO_TRANSITIVE_INFERENCE:

				try{

					String roomSchemaURL = "https://www.dropbox.com/s/2pka3ny3brbsnoi/hands-on-ontology.rdf?dl=1";
					String roomDataURL = "https://www.dropbox.com/s/3zx7kmwy1lru7me/hands-on-ontology-data.rdf?dl=1";

					inputstreamName = generalIRI + "roomstream";
					csparqlAPI.registerStream(inputstreamName);

					query = "REGISTER QUERY ROOMTEST AS " +
							"PREFIX ex:<http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#> " +
							"SELECT ?room ?post (COUNT(?otherpost) AS ?postPerRoom) " +
							"FROM STREAM <" + inputstreamName + "> [RANGE 120s STEP 20s] "	+ 
							"FROM <" + roomSchemaURL + "> " +
							"FROM <" + roomDataURL + "> " +
							"WHERE { " +
							"?otherpost ex:discusses ?post ." +
							"?post ex:where ?room . " +
							"} GROUP BY ?room ?post ";

					queryURI = csparqlAPI.registerQuery("ROOMTEST", query);
					json_results_list.setStartTS(System.currentTimeMillis());

					Client_Server.queryProxyIdTable.put(query, queryURI);

					csparqlAPI.addObserver("ROOMTEST", actual_client_address + ":" + actual_client_port + "/results");

				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} 

				break;

			case TIMISOARA_DEMO:

				try{

					boolean feeding = true;
					boolean registering = true;
					boolean unregistering = false;
					//				String csparqlURL = "http://www.modaclouds.eu/csparql";
					//				String csparqlStreamBaseUri = csparqlURL + "/streams/";
					String csparqlStreamBaseUri = "http://streamreasoning.org/";
					String cloudWatchCollectorStreamIri = csparqlStreamBaseUri + "cloudWatch";
					String mySQLCollectorStreamIri = csparqlStreamBaseUri + "mySQL";
					String ofbizLogCollectorStreamIri = csparqlStreamBaseUri + "ofbizLog";

					Thread.sleep(1000);

					double maxCPU = 0.8;

					String CPUViolationQueryName = "CPUViolationMR";
					String abortedConnectionsQueryName = "abortedConnectionsMR";
					String attemptedConnectionsQueryName = "attemptedConnectionsMR";
					String throughputQueryName = "throughputMR";
					String plotterObserverDispatcherQueryName = "plotterObserverDispatcherQuery";
					String plotterObserverTunnelQueryName = "plotterObserverTunnelQuery";
					String CPUViolationQueryStreamIri = csparqlStreamBaseUri + CPUViolationQueryName;
					String abortedConnectionsQueryStreamIri = csparqlStreamBaseUri + abortedConnectionsQueryName;
					String attemptedConnectionsQueryStreamIri = csparqlStreamBaseUri + attemptedConnectionsQueryName;
					String throughputQueryStreamIri = csparqlStreamBaseUri + throughputQueryName;
					String plotterObserverDispatcherQueryStreamIri = csparqlStreamBaseUri + plotterObserverDispatcherQueryName;
					String plotterObserverTunnelQueryStreamIri = csparqlStreamBaseUri + plotterObserverTunnelQueryName;


					int CPUViolationTimeStep = 10;
					String CPUViolationQuery = "REGISTER STREAM "+ CPUViolationQueryName +" AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + MC.getURI() + "> " +
							"CONSTRUCT { [] rdf:type mc:EnableAction ; " +
							"mc:hasArgument mc:AbortedConnectionsMR ; " +
							"mc:hasArgument mc:AttemptedConnectionsMR ; " +
							"mc:hasArgument mc:ThroughputMR ; " +
							"mc:hasArgument ?infrastructure . } " +
							"FROM STREAM <" + cloudWatchCollectorStreamIri + "> [RANGE 60s STEP "+CPUViolationTimeStep+"s] " +
							"WHERE { { " +
							"SELECT (AVG(?value) AS ?avgCPU) ?infrastructure " +
							"WHERE { " +
							"?datum mc:hasMetric mc:CPUUtilization . " +
							"?datum mc:isAbout ?infrastructure . " +
							"?datum mc:hasValue ?value . " +
							"?platform mc:runsOn ?infrastructure . " +
							"?platform rdf:type mc:Platform . " +
							"?infrastructure rdf:type mc:Infrastructure . " + 
							"} " +
							"GROUP BY ?infrastructure " +
							"HAVING (?avgCPU > \"" + maxCPU + "\"^^xsd:double) " +
							"} }";

					//				EVENTS:
					//				String abortedConnectionsQuery = "REGISTER STREAM "+ abortedConnectionsQueryName +" AS " +
					//						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
					//						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					//						"PREFIX mc: <" + MC.getURI() + "> " +
					//						"CONSTRUCT { [] rdf:type mc:SendAction ; " +
					//						"mc:hasArgument mc:PlotterObserver ; " +
					//						"mc:hasArgument [ rdf:type mc:Variable ; " +
					//						"mc:hasValue ?abortedConnections ; " +
					//						"mc:hasMetric mc:AbortedConnections ; " +
					//						"mc:hasPlatform ?platform ] . } " +
					//						"FROM STREAM <" + mySQLCollectorStreamIri + "> [RANGE 10s STEP 10s] " +
					//						"FROM STREAM <" + CPUViolationQueryStreamIri + "> [RANGE 10s STEP 10s] " +
					//						"WHERE { { " +
					//						"SELECT ?abortedConnections ?platform " +
					//						"WHERE { " +
					//						"?action rdf:type mc:EnableAction . " +
					//						"?action mc:hasArgument mc:AbortedConnectionsMR . " +
					//						"{ SELECT (COUNT(?event) AS ?abortedConnections) ?platform " +
					//						"WHERE { " +
					//						"?action rdf:type mc:EnableAction . " +
					//						"?action mc:hasArgument mc:AbortedConnectionsMR . " +
					//						"?action mc:hasArgument ?infrastructure . " +
					//						"?event rdf:type mc:AbortedConnectionEvent . " +
					//						"?event mc:isAbout ?platform . " +
					//						"?platform mc:runsOn ?infrastructure . " +
					//						"?infrastructure rdf:type mc:Infrastructure . " + 
					//						"?platform rdf:type mc:Platform . " + 
					//						"} " +
					//						"GROUP BY ?platform } " +
					//						"} " +
					//						"} }";


					String abortedConnectionsQuery = "REGISTER STREAM "+ abortedConnectionsQueryName +" AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + MC.getURI() + "> " +
							"CONSTRUCT { [] rdf:type mc:SendAction ; " +
							"mc:hasArgument mc:PlotterObserver ; " +
							"mc:hasArgument [ rdf:type mc:Variable ; " +
							"mc:hasValue ?abortedConnections ; " +
							"mc:hasMetric mc:AbortedConnections ; " +
							"mc:hasPlatform ?platform ] . } " +
							"FROM STREAM <" + mySQLCollectorStreamIri + "> [RANGE 10s STEP 10s] " +
							"FROM STREAM <" + CPUViolationQueryStreamIri + "> [RANGE 10s STEP 10s] " +
							"WHERE { { " +
							"SELECT ?abortedConnections ?platform " +
							"WHERE { " +
							"?action rdf:type mc:EnableAction . " +
							"?action mc:hasArgument mc:AbortedConnectionsMR . " +
							"{ SELECT (SUM(?value) AS ?abortedConnections) ?platform " +
							"WHERE { " +
							"?action rdf:type mc:EnableAction . " +
							"?action mc:hasArgument mc:AbortedConnectionsMR . " +
							"?action mc:hasArgument ?infrastructure . " +
							"?datum rdf:type mc:MonitoringDatum . " +
							"?datum mc:hasMetric mc:AbortedConnections . " +
							"?datum mc:hasValue ?value . " +
							"?datum mc:isAbout ?platform . " +
							"?platform mc:runsOn ?infrastructure . " +
							"?infrastructure rdf:type mc:Infrastructure . " + 
							"?platform rdf:type mc:Platform . " + 
							"} " +
							"GROUP BY ?platform } " +
							"} " +
							"} }";

					//				EVENTS:
					//				String attemptedConnectionsQuery = "REGISTER STREAM "+ attemptedConnectionsQueryName +" AS " +
					//						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
					//						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					//						"PREFIX mc: <" + MC.getURI() + "> " +
					//						"CONSTRUCT { [] rdf:type mc:SendAction ; " +
					//						"mc:hasArgument mc:PlotterObserver ; " +
					//						"mc:hasArgument [ rdf:type mc:Variable ; " +
					//						"mc:hasValue ?attemptedConnections ; " +
					//						"mc:hasMetric mc:AttemptedConnections ; " +
					//						"mc:hasPlatform ?platform ] . } " +
					//						"FROM STREAM <" + mySQLCollectorStreamIri + "> [RANGE 10s STEP 10s] " +
					//						"FROM STREAM <" + CPUViolationQueryStreamIri + "> [RANGE 10s STEP 10s] " +
					//						"WHERE { { " +
					//						"SELECT ?attemptedConnections ?platform " +
					//						"WHERE { " +
					//						"?action rdf:type mc:EnableAction . " +
					//						"?action mc:hasArgument mc:AttemptedConnectionsMR . " +
					//						"{ SELECT (COUNT(?event) AS ?attemptedConnections) ?platform " +
					//						"WHERE { " +
					//						"?action rdf:type mc:EnableAction . " +
					//						"?action mc:hasArgument mc:AttemptedConnectionsMR . " +
					//						"?action mc:hasArgument ?infrastructure . " +
					//						"?event rdf:type mc:AttemptedConnectionEvent . " +
					//						"?event mc:isAbout ?platform . " +
					//						"?platform mc:runsOn ?infrastructure . " +
					//						"?infrastructure rdf:type mc:Infrastructure . " + 
					//						"?platform rdf:type mc:Platform . " + 
					//						"} " +
					//						"GROUP BY ?platform } " +
					//						"} " +
					//						"} }";

					String attemptedConnectionsQuery = "REGISTER STREAM "+ attemptedConnectionsQueryName +" AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + MC.getURI() + "> " +
							"CONSTRUCT { [] rdf:type mc:SendAction ; " +
							"mc:hasArgument mc:PlotterObserver ; " +
							"mc:hasArgument [ rdf:type mc:Variable ; " +
							"mc:hasValue ?attemptedConnections ; " +
							"mc:hasMetric mc:AttemptedConnections ; " +
							"mc:hasPlatform ?platform ] . } " +
							"FROM STREAM <" + mySQLCollectorStreamIri + "> [RANGE 10s STEP 10s] " +
							"FROM STREAM <" + CPUViolationQueryStreamIri + "> [RANGE 10s STEP 10s] " +
							"WHERE { { " +
							"SELECT ?attemptedConnections ?platform " +
							"WHERE { " +
							"?action rdf:type mc:EnableAction . " +
							"?action mc:hasArgument mc:AttemptedConnectionsMR . " +
							"{ SELECT (SUM(?value) AS ?attemptedConnections) ?platform " +
							"WHERE { " +
							"?action rdf:type mc:EnableAction . " +
							"?action mc:hasArgument mc:AttemptedConnectionsMR . " +
							"?action mc:hasArgument ?infrastructure . " +
							"?datum rdf:type mc:MonitoringDatum . " +
							"?datum mc:hasMetric mc:AttemptedConnections . " +
							"?datum mc:hasValue ?value . " +
							"?datum mc:isAbout ?platform . " +
							"?platform mc:runsOn ?infrastructure . " +
							"?infrastructure rdf:type mc:Infrastructure . " + 
							"?platform rdf:type mc:Platform . " + 
							"} " +
							"GROUP BY ?platform } " +
							"} " +
							"} }";

					//				EVENTS:
					//				String throughputQuery = "REGISTER STREAM "+ throughputQueryName +" AS " +
					//						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
					//						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					//						"PREFIX mc: <" + MC.getURI() + "> " +
					//						"CONSTRUCT { [] rdf:type mc:SendAction ; " +
					//						"mc:hasArgument mc:PlotterObserver ; " +
					//						"mc:hasArgument [ rdf:type mc:Variable ; " +
					//						"mc:hasValue ?throughput ; " +
					//						"mc:hasMetric mc:Throughput ; " +
					//						"mc:hasOperation ?operation ] . } " +
					//						"FROM STREAM <" + ofbizLogCollectorStreamIri + "> [RANGE 10s STEP 10s] " +
					//						"FROM STREAM <" + CPUViolationQueryStreamIri + "> [RANGE 10s STEP 10s] " +
					//						"WHERE { { " +
					//						"SELECT ?throughput ?operation " +
					//						"WHERE { " +
					//						"?action rdf:type mc:EnableAction . " +
					//						"?action mc:hasArgument mc:ThroughputMR . " +
					//						"{ SELECT (COUNT(?request)/10 AS ?throughput) ?operation " +
					//						"WHERE { " +
					//						"?action rdf:type mc:EnableAction . " +
					//						"?action mc:hasArgument mc:ThroughputMR . " +
					//						"?action mc:hasArgument ?infrastructure . " +
					//						"?datum rdf:type mc:SuccessfulRequestEvent . " +
					//						"?datum mc:isAbout ?request . " +
					//						"?request mc:asksFor ?operation . " +
					//						"?operation mc:isProvidedBy ?software . " + 
					//						"?software mc:runsOn ?infrastructure . " + 
					//						"} " +
					//						"GROUP BY ?operation } " +
					//						"} " +
					//						"} }";

					int throughputTimeWindow = 10;
					String throughputQuery = "REGISTER STREAM "+ throughputQueryName +" AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + MC.getURI() + "> " +
							"CONSTRUCT { [] rdf:type mc:SendAction ; " +
							"mc:hasArgument mc:PlotterObserver ; " +
							"mc:hasArgument [ rdf:type mc:Variable ; " +
							"mc:hasValue ?throughput ; " +
							"mc:hasMetric mc:Throughput ; " +
							"mc:hasOperation ?operation ] . } " +
							"FROM STREAM <" + ofbizLogCollectorStreamIri + "> [RANGE "+throughputTimeWindow+"s STEP 10s] " +
							"FROM STREAM <" + CPUViolationQueryStreamIri + "> [RANGE 10s STEP 10s] " +
							"WHERE { { " +
							"SELECT ?throughput ?operation " +
							"WHERE { " +
							"?action rdf:type mc:EnableAction . " +
							"?action mc:hasArgument mc:ThroughputMR . " +
							"{ SELECT (SUM(?value)/"+throughputTimeWindow+" AS ?throughput) ?operation " +
							"WHERE { " +
							"?action rdf:type mc:EnableAction . " +
							"?action mc:hasArgument mc:ThroughputMR . " +
							"?action mc:hasArgument ?infrastructure . " +
							"?datum rdf:type mc:MonitoringDatum . " +
							"?datum mc:hasValue ?value . " +
							"?datum mc:hasMetric mc:SuccessfulRequests . " +
							"?datum mc:isAbout ?request . " +
							"?request mc:asksFor ?operation . " +
							"?operation mc:isProvidedBy ?software . " + 
							"?software mc:runsOn ?infrastructure . " + 
							"} " +
							"GROUP BY ?operation } " +
							"} " +
							"} }";

					String plotterObserverDispatcherQuery = "REGISTER STREAM "+ plotterObserverDispatcherQueryName +" AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + MC.getURI() + "> " +
							"PREFIX f: <http://larkc.eu/csparql/sparql/jena/ext#> " +
							"SELECT ?provider ?infrastrucureClass ?infrastructure ?platformClass ?platform " +
							"?softwareClass ?software ?operationClass ?operation ?metric ?value ?timestamp " +
							"FROM STREAM <" + abortedConnectionsQueryStreamIri + "> [RANGE 10s STEP 10s] " +
							"FROM STREAM <" + attemptedConnectionsQueryStreamIri + "> [RANGE 10s STEP 10s] " +
							"FROM STREAM <" + throughputQueryStreamIri + "> [RANGE 10s STEP 10s] " +
							"WHERE { { " +
							"SELECT ?provider ?infrastrucureClass ?infrastructure ?platformClass ?platform " +
							"?softwareClass ?software ?operationClass ?operation ?metric ?value (f:timestamp(?action, mc:hasArgument, ?variable) AS ?timestamp) " +
							"WHERE { " +
							"?action rdf:type mc:SendAction . " +
							"?action mc:hasArgument mc:PlotterObserver . " +
							"?action mc:hasArgument ?variable . " +
							"?variable mc:hasValue ?value . " +
							"?variable mc:hasMetric ?metric . " +
							"OPTIONAL { ?variable mc:hasProvider ?provider } . " +
							"OPTIONAL { ?variable mc:hasInfrastructureClass ?infrastructureClass } . " +
							"OPTIONAL { ?variable mc:hasInfrastructure ?infrastructure } . " +
							"OPTIONAL { ?variable mc:hasPlatformClass ?platformClass } . " +
							"OPTIONAL { ?variable mc:hasPlatform ?platform } . " +
							"OPTIONAL { ?variable mc:hasSoftwareClass ?softwareClass } . " +
							"OPTIONAL { ?variable mc:hasSoftware ?software } . " +
							"OPTIONAL { ?variable mc:hasOperationClass ?operationClass } . " +
							"OPTIONAL { ?variable mc:hasOperation ?operation } " +
							"} " +
							"} } ";


					String plotterObserverTunnelQuery = "REGISTER STREAM "+ plotterObserverTunnelQueryName +" AS " +
							"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX mc: <" + MC.getURI() + "> " +
							"PREFIX f: <http://larkc.eu/csparql/sparql/jena/ext#> " +
							"SELECT ?provider ?infrastrucureClass ?infrastructure ?platformClass ?platform " +
							"?softwareClass ?software ?operationClass ?operation ?metric ?value ?timestamp " +
							"FROM STREAM <" + cloudWatchCollectorStreamIri + "> [RANGE 10s STEP 10s] " +
							"WHERE { { " +
							"SELECT ?provider ?infrastrucureClass ?infrastructure ?platformClass ?platform " +
							"?softwareClass ?software ?operationClass ?operation ?metric ?value (f:timestamp(?datum, mc:hasMetric, ?metric) AS ?timestamp) " +
							"WHERE { " +
							"?datum rdf:type mc:MonitoringDatum . " +
							"?datum mc:hasMetric ?metric . " +
							"OPTIONAL { ?datum mc:isAbout ?infrastructure . ?infrastructure rdf:type mc:Infrastructure } . " + //subclassof mc:infrastructure, rdf:type infraClass
							"?datum mc:hasValue ?value . " +
							"} " +
							"} } ";

					String CPUViolationQueryID = CPUViolationQueryName;
					String abortedConnectionsQueryID = abortedConnectionsQueryName;
					String attemptedConnectionsQueryID = attemptedConnectionsQueryName;
					String throughputQueryID = throughputQueryName;
					String plotterObserverDispatcherQueryID = plotterObserverDispatcherQueryName;
					String plotterObserverTunnelQueryID = plotterObserverTunnelQueryName;

					if (unregistering){
						System.out.println(csparqlAPI.unregisterStream(cloudWatchCollectorStreamIri));
						System.out.println(csparqlAPI.unregisterStream(mySQLCollectorStreamIri));
						System.out.println(csparqlAPI.unregisterStream(ofbizLogCollectorStreamIri));
						System.out.println(csparqlAPI.unregisterQuery(CPUViolationQueryID));
						System.out.println(csparqlAPI.unregisterQuery(abortedConnectionsQueryID));
						System.out.println(csparqlAPI.unregisterQuery(attemptedConnectionsQueryID));
						System.out.println(csparqlAPI.unregisterQuery(throughputQueryID));
						System.out.println(csparqlAPI.unregisterQuery(plotterObserverDispatcherQueryID));
						System.out.println(csparqlAPI.unregisterQuery(plotterObserverTunnelQueryID));
					}
					if (registering) {
						System.out.println(csparqlAPI.registerStream(cloudWatchCollectorStreamIri));
						System.out.println(csparqlAPI.registerStream(mySQLCollectorStreamIri));
						System.out.println(csparqlAPI.registerStream(ofbizLogCollectorStreamIri));


						CPUViolationQueryID = csparqlAPI.registerQuery(CPUViolationQueryName, CPUViolationQuery);
						json_results_list.setStartTS(System.currentTimeMillis());

						System.out.println(CPUViolationQueryID);

						abortedConnectionsQueryID = csparqlAPI.registerQuery(abortedConnectionsQueryName, abortedConnectionsQuery);
						json_results_list.setStartTS(System.currentTimeMillis());

						System.out.println(abortedConnectionsQueryID);

						Thread.sleep(1000);

						attemptedConnectionsQueryID = csparqlAPI.registerQuery(attemptedConnectionsQueryName, attemptedConnectionsQuery);
						json_results_list.setStartTS(System.currentTimeMillis());

						System.out.println(attemptedConnectionsQueryID);

						Thread.sleep(1000);

						throughputQueryID = csparqlAPI.registerQuery(throughputQueryName, throughputQuery);
						json_results_list.setStartTS(System.currentTimeMillis());

						System.out.println(throughputQueryID);

						Thread.sleep(1000);

						plotterObserverDispatcherQueryID = csparqlAPI.registerQuery(plotterObserverDispatcherQueryName, plotterObserverDispatcherQuery);
						json_results_list.setStartTS(System.currentTimeMillis());

						System.out.println(plotterObserverDispatcherQueryID);

						Thread.sleep(1000);

						plotterObserverTunnelQueryID = csparqlAPI.registerQuery(plotterObserverTunnelQueryName, plotterObserverTunnelQuery);
						json_results_list.setStartTS(System.currentTimeMillis());

						System.out.println(plotterObserverTunnelQueryID);
					}
					if (feeding) {

						String urlToPlotterObserver = "http://localhost:8176/results";


						//			csparqlAPI.addObserver(CPUViolationQueryID, urlToPlotterObserver); //test
						//			csparqlAPI.addObserver(abortedConnectionsQueryID, urlToPlotterObserver); //test
						//			csparqlAPI.addObserver(attemptedConnectionsQueryID, urlToPlotterObserver); //test
						//			csparqlAPI.addObserver(throughputQueryID, urlToPlotterObserver); //test
						csparqlAPI.addObserver(plotterObserverDispatcherQueryID, urlToPlotterObserver);
						csparqlAPI.addObserver(plotterObserverTunnelQueryID, urlToPlotterObserver);

						Client_Server.queryProxyIdTable.put(CPUViolationQuery, CPUViolationQueryID);
						Client_Server.queryProxyIdTable.put(abortedConnectionsQuery, abortedConnectionsQueryID);
						Client_Server.queryProxyIdTable.put(attemptedConnectionsQuery, attemptedConnectionsQueryID);
						Client_Server.queryProxyIdTable.put(throughputQuery, throughputQueryID);
						Client_Server.queryProxyIdTable.put(plotterObserverDispatcherQuery, plotterObserverDispatcherQueryID);
						Client_Server.queryProxyIdTable.put(plotterObserverTunnelQuery, plotterObserverTunnelQueryID);


						String urlToAmazonBackEndVM = "http://backend.amazon.com";
						String urlToFlexiBackEndVM = "http://backend.flexi.com";
						String urlToAmazonFrontEndVM = "http://backend.amazon.com";
						String urlToFlexiFrontEndVM = "http://backend.flexi.com";
						String mySQLPort = "8080";
						String OFBizPort = "80";
						String urlToAmazonOfbiz = urlToAmazonFrontEndVM + ":" + OFBizPort;
						String urlToFlexiOfbiz = urlToFlexiFrontEndVM + ":" + OFBizPort;

						String amazonBackEndVMIRI = urlToAmazonBackEndVM + "/" + MC.AmazonBackEndVM.getLocalName() + "#1";
						String flexiBackEndVMIRI = urlToFlexiBackEndVM + "/" + MC.FlexiBackEndVM.getLocalName() + "#1";
						String amazonFrontEndVMIRI = urlToAmazonFrontEndVM + "/" + MC.AmazonFrontEndVM.getLocalName() + "#1";
						String flexiFrontEndVMIRI = urlToFlexiFrontEndVM + "/" + MC.FlexiFrontEndVM.getLocalName() + "#1";
						String amazonMySQLIRI = urlToAmazonBackEndVM + ":"+mySQLPort+"/" + MC.MySQL.getLocalName() + "#1";
						String flexiMySQLIRI = urlToFlexiBackEndVM + ":"+mySQLPort+"/" + MC.MySQL.getLocalName() + "#1";
						String amazonOFBizIRI = urlToAmazonOfbiz + "/" + MC.OFBiz.getLocalName() + "#1";
						String flexiOFBizIRI = urlToFlexiOfbiz + "/" + MC.OFBiz.getLocalName() + "#1";
						String amazonJVMIRI = urlToAmazonFrontEndVM + "/" + MC.JVM.getLocalName() + "#1";
						String flexiJVMIRI = urlToFlexiFrontEndVM + "/" + MC.JVM.getLocalName() + "#1";

						long cloudWatchTimeStep = 2000;
						double amazonBackEndMeanCpuValue = 0.9;
						double flexiBackEndMeanCpuValue = 0.5;
						double amazonFrontEndMeanCpuValue = 0.3;
						double flexiFrontEndMeanCpuValue = 0.9;
						new Thread(new CloudWatchCollectorStreamer(csparqlServerAddress, cloudWatchCollectorStreamIri, cloudWatchTimeStep, 
								amazonBackEndVMIRI, amazonMySQLIRI, null, amazonBackEndMeanCpuValue)).start();
						new Thread(new CloudWatchCollectorStreamer(csparqlServerAddress, cloudWatchCollectorStreamIri, cloudWatchTimeStep, 
								flexiBackEndVMIRI, flexiMySQLIRI, null, flexiBackEndMeanCpuValue)).start();
						new Thread(new CloudWatchCollectorStreamer(csparqlServerAddress, cloudWatchCollectorStreamIri, cloudWatchTimeStep, 
								amazonFrontEndVMIRI, null, amazonOFBizIRI, amazonFrontEndMeanCpuValue)).start();
						new Thread(new CloudWatchCollectorStreamer(csparqlServerAddress, cloudWatchCollectorStreamIri, cloudWatchTimeStep, 
								flexiFrontEndVMIRI, flexiOFBizIRI, null, flexiFrontEndMeanCpuValue)).start();

						int MySQLTimeStep = 2000;
						double amazonMySQLProbFailure = 0.3;
						new Thread(new MySQLAggregatedCollectorStreamer(csparqlServerAddress, mySQLCollectorStreamIri, MC.AmazonBackEndVM, 
								amazonBackEndVMIRI, amazonMySQLIRI, MySQLTimeStep, amazonMySQLProbFailure)).start();


						double flexiMySQLProbFailure = 0.2;
						new Thread(new MySQLAggregatedCollectorStreamer(csparqlServerAddress, mySQLCollectorStreamIri, MC.FlexiBackEndVM, 
								flexiBackEndVMIRI, flexiMySQLIRI, MySQLTimeStep, flexiMySQLProbFailure)).start();


						int OfbizCollectorTimeStep = 3000;
						new Thread(new OFBizLogAggregatedCollectorStreamer(csparqlServerAddress, ofbizLogCollectorStreamIri, urlToAmazonOfbiz, 
								amazonFrontEndVMIRI, amazonJVMIRI, amazonOFBizIRI, OfbizCollectorTimeStep)).start();

						new Thread(new OFBizLogAggregatedCollectorStreamer(csparqlServerAddress, ofbizLogCollectorStreamIri, urlToFlexiOfbiz, 
								flexiFrontEndVMIRI, flexiJVMIRI, flexiOFBizIRI, OfbizCollectorTimeStep)).start();



						//			int amazonMySQLMTBConnections = 1000;
						//			int amazonMySQLDeltaMTBConn = 500;
						//			double amazonMySQLProbFailure = 0.3;
						//			new Thread(new MySQLEventsCollectorStreamer(csparqlServerAddress, mySQLCollectorStreamIri, MC.AmazonBackEndVM, 
						//					amazonBackEndVMIRI, amazonMySQLIRI, amazonMySQLMTBConnections, amazonMySQLDeltaMTBConn, amazonMySQLProbFailure)).start();
						//	
						//			
						//			
						//			int flexiMySQLMTBConnections = 1200;
						//			int flexiMySQLDeltaMTBConn = 700;
						//			double flexiMySQLProbFailure = 0.2;
						//			new Thread(new MySQLEventsCollectorStreamer(csparqlServerAddress, mySQLCollectorStreamIri, MC.FlexiBackEndVM, 
						//					flexiBackEndVMIRI, flexiMySQLIRI, flexiMySQLMTBConnections, flexiMySQLDeltaMTBConn, flexiMySQLProbFailure)).start();
						//	
						//			
						//			int amazonOFBizMTBRequests = 300;
						//			int amazonOFBizDeltaMTBReqs = 100;
						//			double amazonOFBizProbFailure = 0.5;
						//			new Thread(new OFBizLogEventsCollectorStreamer(csparqlServerAddress, ofbizLogCollectorStreamIri, urlToAmazonOfbiz, 
						//					amazonFrontEndVMIRI, amazonJVMIRI, amazonOFBizIRI, amazonOFBizMTBRequests, amazonOFBizDeltaMTBReqs, amazonOFBizProbFailure)).start();
						//		
						//			
						//			int flexiOFBizMTBRequests = 200;
						//			int flexiOFBizDeltaMTBReqs = 100;
						//			double flexiOFBizProbFailure = 0.2;
						//			new Thread(new OFBizLogEventsCollectorStreamer(csparqlServerAddress, ofbizLogCollectorStreamIri, urlToFlexiOfbiz, 
						//					flexiFrontEndVMIRI, flexiJVMIRI, flexiOFBizIRI, flexiOFBizMTBRequests, flexiOFBizDeltaMTBReqs, flexiOFBizProbFailure)).start();


					}
				} catch (ServerErrorException e) {
					logger.error("rsp_server4csparql_server error", e);
				} catch (InterruptedException e) {
					logger.error("Error while launching the sleep operation", e);
				}

				break;

			default:
				System.exit(0);
				break;
			}

			try {
				Thread.sleep(1500000);
			} catch (InterruptedException e) {
				logger.error("Error while launching the sleep operation", e);
			}

			csparqlAPI.unregisterStream(inputstreamName);
		}

	}

	public Restlet createInboundRoot(){

		getContext().getAttributes().put("queryProxyIdTable", Client_Server.queryProxyIdTable);
		getContext().getAttributes().put("json_results_list", Client_Server.json_results_list);

		Router router = new Router(getContext());
		router.setDefaultMatchingMode(Template.MODE_EQUALS);

		router.attach("/results", ResultsManipulator_Oracle.class);

		return router;
	}

	public static int nextID() {
		return ID++;
	}

}
