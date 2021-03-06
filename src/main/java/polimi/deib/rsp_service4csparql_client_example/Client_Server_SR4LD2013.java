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

import polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.rsp_service4csparql_client_example.configuration.Config;
import polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities.List_of_Sparql_json_results_oracle;
import polimi.deib.rsp_service4csparql_client_example.results_manipulator.ResultsManipulator_Oracle;
import polimi.deib.rsp_service4csparql_client_example.streamer.BaseStreamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.ISWC_DemoStreamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.Inference_Streamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.MultiThreadStreamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.SR4LD_Streamer;
import polimi.deib.rsp_service4csparql_client_example.streamer.Static_Knowledge_Test_Streamer;
import polimi.deib.timisoara_demo.ontology.MC;
import polimi.deib.timisoara_demo.streamer.CloudWatchCollectorStreamer;
import polimi.deib.timisoara_demo.streamer.MySQLAggregatedCollectorStreamer;
import polimi.deib.timisoara_demo.streamer.OFBizLogAggregatedCollectorStreamer;

public class Client_Server_SR4LD2013 extends Application{

	private static Component component;
	private static Hashtable<String, String> queryProxyIdTable = new Hashtable<String, String>();
	private static Logger logger = LoggerFactory.getLogger(Client_Server_SR4LD2013.class.getName());

	private static int ID = 0;

	private static List_of_Sparql_json_results_oracle json_results_list = new List_of_Sparql_json_results_oracle();
	private String generalIRI = "http://streamreasoning.org/ontologies/sr4ld2013-onto#";



	public static void main(String[] args) throws Exception{

		//Set to false if you need client server only for writing results
		boolean startExample = true;

		final int QUERY1 = 0;

		int key = QUERY1;
		String csparqlServerAddress = "http://localhost:8175";

		String actual_client_address;
		int actual_client_port;

		component = new Component();
		component.getServers().add(Protocol.HTTP, Config.getInstance().getServerPort());
		component.getClients().add(Protocol.FILE);  

		Client_Server_SR4LD2013 csparqlServer = new Client_Server_SR4LD2013();
		component.getDefaultHost().attach("", csparqlServer);

		component.start();

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

		String generalIRI = "http://myexample.org/ ";

		RSP_services_csparql_API csparqlAPI = new RSP_services_csparql_API(csparqlServerAddress);

		csparqlAPI.registerStream("http://streamreasoning.org/streams/fs");
		csparqlAPI.registerStream("http://streamreasoning.org/streams/fs ");
		csparqlAPI.registerStream("http://streamreasoning.org/streams/rfid");

		switch(key){

		case QUERY1:
			try {

				inputstreamName = generalIRI + "onlinefeeding";

				csparqlAPI.registerStream(inputstreamName);
				
				query = "REGISTER QUERY WhoIsWhereOnFb AS " +
						"PREFIX : <http://streamreasoning.org/ontologies/sr4ld2013-onto#> " +
						"SELECT ?room ?person " +
						"FROM STREAM <http://streamreasoning.org/streams/fb> [RANGE 1m STEP 10s]" +
						"WHERE {?person1 :posts [ :who ?person ; :where ?room ] . " +
						"}";

				queryURI = csparqlAPI.registerQuery("WhoIsWhereOnFb", query);
				csparqlAPI.addObserver(queryURI, actual_client_address + ":" + actual_client_port + "/results");

				new Thread(new SR4LD_Streamer(csparqlAPI)).start(); 

			} catch (ServerErrorException e) {
				logger.error("rsp_server4csparql_server error", e);
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

	public Restlet createInboundRoot(){

		getContext().getAttributes().put("queryProxyIdTable", Client_Server_SR4LD2013.queryProxyIdTable);
		getContext().getAttributes().put("json_results_list", Client_Server_SR4LD2013.json_results_list);

		Router router = new Router(getContext());
		router.setDefaultMatchingMode(Template.MODE_EQUALS);

		router.attach("/results", ResultsManipulator_Oracle.class);

		return router;
	}

	public static int nextID() {
		return ID++;
	}

}
