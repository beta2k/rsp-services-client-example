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
package polimi.deib.timisoara_demo.streamer;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;
import polimi.deib.rsp_service4csparql_client_example.Client_Server;
import polimi.deib.timisoara_demo.ontology.MC;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class MySQLEventsCollectorStreamer implements Runnable {

	private String csparqlServerAddress = "http://localhost:8175";
	private Csparql_Remote_API csparqlAPI = new Csparql_Remote_API(csparqlServerAddress);
	private String streamIRI;
	private String abortedEventBaseIRI;
	private String attemptedEventBaseIRI;
	private String mySQLIRI;

	private Logger logger = LoggerFactory.getLogger(MySQLEventsCollectorStreamer.class.getName());
	private int MTBConnections;
	private int deltaMTBConn;
	private String infrastructureIRI;
	private double probFailure;

	public MySQLEventsCollectorStreamer(String streamIRI, Resource infrastructureClass, String infrastructureIRI, String mySQLIRI, int MTBConnections, int deltaMTBConn, double probFailure) {
		super();
		this.streamIRI = streamIRI;
		this.abortedEventBaseIRI = streamIRI + "/" + MC.AbortedConnectionEvent.getLocalName() + "#";
		this.attemptedEventBaseIRI = streamIRI + "/" + MC.AttemptedConnectionEvent.getLocalName() + "#";
		this.mySQLIRI = mySQLIRI;
		this.MTBConnections = MTBConnections;
		this.deltaMTBConn = deltaMTBConn;
		this.infrastructureIRI = infrastructureIRI;
		this.probFailure = probFailure;
	}

	public void run() {

		Model m;

		int sleepTime;

		Random randomGenerator = new Random();
		int id;
		while(true){
			
			id = Client_Server.nextID();
			
			sleepTime = MTBConnections + (2*randomGenerator.nextInt(deltaMTBConn)-deltaMTBConn);
			m = ModelFactory.createDefaultModel();
			
			//ATTEMPTED CONNECTIONS
			m.createResource(attemptedEventBaseIRI + id)
				.addProperty(RDF.type, MC.AttemptedConnectionEvent)
				.addProperty(MC.isAbout, m.createResource(mySQLIRI)
						.addProperty(RDF.type, MC.Platform) // .addProperty(RDF.type, MC.MySQL)
						.addProperty(MC.runsOn, m.createResource(infrastructureIRI)
								.addProperty(RDF.type, MC.Infrastructure))); // infrastructureClass
			
			//ABORTED CONNECTIONS
			if (randomGenerator.nextDouble() < probFailure) {
				m.createResource(abortedEventBaseIRI + id)
					.addProperty(RDF.type, MC.AbortedConnectionEvent)
					.addProperty(MC.isAbout, m.createResource(mySQLIRI)
							.addProperty(RDF.type, MC.Platform) // .addProperty(RDF.type, MC.MySQL)
							.addProperty(MC.runsOn, m.createResource(infrastructureIRI)
									.addProperty(RDF.type, MC.Infrastructure))); // infrastructureClass
			}
			

			csparqlAPI.feedStream(streamIRI, m);
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("Error while launching the sleep operation", e);
			}

		}
	}
}
