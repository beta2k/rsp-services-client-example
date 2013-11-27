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
package polimi.deib.timisoara_demo.streamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.csparql_rest_api.exception.StreamErrorException;
import polimi.deib.rsp_service4csparql_client_example.Client_Server;
import polimi.deib.timisoara_demo.ontology.MC;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;

public class OFBizLogAggregatedCollectorStreamer implements Runnable {

	private String csparqlServerAddress;
	private RSP_services_csparql_API csparqlAPI;
	private String streamIRI;
	private String datumBaseIRI;
	private String requestBaseIRI;
	private String jvmIRI;
	private String ofbizIRI;

	private Logger logger = LoggerFactory.getLogger(OFBizLogAggregatedCollectorStreamer.class.getName());
	private String infrastructureIRI;
	private String urlToOFBiz;
	private Random randomGenerator;
	private int sleepTime;

	public OFBizLogAggregatedCollectorStreamer(String csparqlServerAddress, String streamIRI, String urlToOFBiz, String infrastructureIRI, String jvmIRI, String ofbizIRI, int sleepTime) {
		super();
		this.csparqlServerAddress = csparqlServerAddress;
		csparqlAPI = new RSP_services_csparql_API(csparqlServerAddress);
		this.streamIRI = streamIRI;
		this.datumBaseIRI = streamIRI + "/" + MC.MonitoringDatum.getLocalName() + "#";
		this.requestBaseIRI = streamIRI + "/" + MC.Request.getLocalName() + "#";
		this.urlToOFBiz = urlToOFBiz;
		this.jvmIRI = jvmIRI;
		this.ofbizIRI = ofbizIRI;
		this.infrastructureIRI = infrastructureIRI;
		randomGenerator = new Random();
		this.sleepTime = sleepTime;
	}

	public void run() {

		Model m;
		
		List<String> operationsIris = new ArrayList<String>();
		operationsIris.add(urlToOFBiz + "/" + MC.Login.getLocalName() + "#1");
		operationsIris.add(urlToOFBiz + "/" + MC.Logout.getLocalName() + "#1");
		operationsIris.add(urlToOFBiz + "/" + MC.Checkout.getLocalName() + "#1");
		
		int id;
		
		while(true){
			
			id = Client_Server.nextID();
			int successfulRequests = randomGenerator.nextInt(50);
			m = ModelFactory.createDefaultModel();
			
			//SUCCESSFUL REQUEST
//			if (randomGenerator.nextDouble() > probFailure) {
				
				m.createResource(datumBaseIRI + id)
					.addProperty(RDF.type, MC.MonitoringDatum)
					.addProperty(MC.hasMetric, MC.SuccessfulRequests)
					.addProperty(MC.hasValue, m.createTypedLiteral(successfulRequests, XSDDatatype.XSDinteger))
					.addProperty(MC.isAbout, m.createResource(requestBaseIRI + id)
						.addProperty(RDF.type, MC.Request)
						.addProperty(MC.isProcessedBy, m.createResource(ofbizIRI)
								.addProperty(RDF.type, MC.Software)
								.addProperty(MC.runsOn, m.createResource(jvmIRI)
										.addProperty(RDF.type, MC.Platform)
										.addProperty(MC.runsOn, m.createResource(infrastructureIRI)
												.addProperty(RDF.type, MC.Infrastructure)))
								.addProperty(MC.runsOn, m.createResource(infrastructureIRI)))
						.addProperty(MC.asksFor, m.createResource(randomGet(operationsIris))
								.addProperty(RDF.type, MC.Operation)
								.addProperty(MC.isProvidedBy, m.createResource(ofbizIRI))));
				
//			}
			
			// RESPONSE TIME
			m.createResource(datumBaseIRI + id+1)
				.addProperty(RDF.type, MC.MonitoringDatum)
				.addProperty(MC.hasMetric, MC.ResponseTime)
				.addProperty(MC.hasValue, m.createTypedLiteral(randomGenerator.nextInt(1000), XSDDatatype.XSDinteger))
				.addProperty(MC.isAbout, m.createResource(requestBaseIRI + id));

			try{
				csparqlAPI.feedStream(streamIRI, m);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (StreamErrorException e) {
				logger.error("StreamErrorException Occurred", e);
			} catch (ServerErrorException e) {
				logger.error("ServerErrorException Occurred", e);
			}

		}
	}

	private String randomGet(List<String> operationsIris) {
		String op = operationsIris.get(randomGenerator.nextInt(operationsIris.size()));
		return op;
	}
}
