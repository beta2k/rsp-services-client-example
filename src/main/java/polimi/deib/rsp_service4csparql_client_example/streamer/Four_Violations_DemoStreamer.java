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
package polimi.deib.rsp_service4csparql_client_example.streamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.csparql_rest_api.exception.StreamErrorException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class Four_Violations_DemoStreamer implements Runnable {

	private RSP_services_csparql_API csparqlAPI;
	private String streamName;
	private long sleepTime;
	private String generalIRI;

	private Logger logger = LoggerFactory.getLogger(Four_Violations_DemoStreamer.class.getName());

	public Four_Violations_DemoStreamer(RSP_services_csparql_API csparqlAPI, String streamName, long sleepTime, String generalIRI) {
		super();
		this.csparqlAPI = csparqlAPI;
		this.streamName = streamName;
		this.sleepTime = sleepTime;
		this.generalIRI = generalIRI;
	}

	public void run() {

		Model m;

		int i = 1;

		while(true){

			m = ModelFactory.createDefaultModel();
			m.add(new ResourceImpl(generalIRI + "violation" + i), new PropertyImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new ResourceImpl("http://www.modaclouds.eu/monitoring#ViolationEvent"));
			i++;
			m.add(new ResourceImpl(generalIRI + "violation" + i), new PropertyImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new ResourceImpl("http://www.modaclouds.eu/monitoring#ViolationEvent"));
			i++;
			m.add(new ResourceImpl(generalIRI + "violation" + i), new PropertyImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new ResourceImpl("http://www.modaclouds.eu/monitoring#ViolationEvent"));
			i++;
			m.add(new ResourceImpl(generalIRI + "violation" + i), new PropertyImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new ResourceImpl("http://www.modaclouds.eu/monitoring#ViolationEvent"));
			i++;
			m.add(new ResourceImpl(generalIRI + "violation" + i), new PropertyImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new ResourceImpl("http://www.modaclouds.eu/monitoring#ViolationEvent"));
			i++;

			try {
				csparqlAPI.feedStream(streamName, m);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("Error while launching the sleep operation", e);
			} catch (StreamErrorException e) {
				logger.error("StreamErrorException Occurred", e);
			} catch (ServerErrorException e) {
				logger.error("ServerErrorException Occurred", e);
			}

		}
	}

}
