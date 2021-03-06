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

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

import polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.csparql_rest_api.exception.StreamErrorException;

public class SR4LD_Streamer implements Runnable {

	private RSP_services_csparql_API csparqlAPI;
	private String generalIRI = "http://streamreasoning.org/ontologies/sr4ld2013-onto#";
	private boolean keepRunning = true;

	private Logger logger = LoggerFactory.getLogger(SR4LD_Streamer.class.getName());

	public SR4LD_Streamer(RSP_services_csparql_API csparqlAPI) {
		super();
		this.csparqlAPI = csparqlAPI;

	}

	public void run() {

		Model m;
		AnonId node;

		while(keepRunning){

			try {

				//FirstRound
				
				Thread.sleep(10000);

				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "RedSensor"), new PropertyImpl(generalIRI + "observes"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Alice "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "where"), new ResourceImpl(generalIRI + "RedRoom "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/rfid", m);
				
				Thread.sleep(10000);
							
				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "Bob "), new PropertyImpl(generalIRI + "posts"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Bob "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "where"), new ResourceImpl(generalIRI + "BlueRoom "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/fs", m);
				
				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "Carl "), new PropertyImpl(generalIRI + "posts"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Carl "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Bob "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/fb", m);
				
				Thread.sleep(20000);
				
				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "David"), new PropertyImpl(generalIRI + "posts"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "David "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Elena "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "where"), new ResourceImpl(generalIRI + "RedRoom "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/fb", m);				
				
				Thread.sleep(60000);
				
				//Second Round
				
				Thread.sleep(10000);

				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "RedSensor"), new PropertyImpl(generalIRI + "observes"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Alice "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "where"), new ResourceImpl(generalIRI + "BlueRoom "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/rfid", m);
				
				Thread.sleep(10000);
							
				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "Bob "), new PropertyImpl(generalIRI + "posts"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Bob "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "where"), new ResourceImpl(generalIRI + "RedRoom "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/fs", m);
				
				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "Carl "), new PropertyImpl(generalIRI + "posts"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Carl "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Bob "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/fb", m);
				
				Thread.sleep(20000);
				
				m = ModelFactory.createDefaultModel();
				node = new AnonId();
				m.add(new ResourceImpl(generalIRI + "David"), new PropertyImpl(generalIRI + "posts"), new ResourceImpl(node));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "David "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "who"), new ResourceImpl(generalIRI + "Elena "));
				m.add(new ResourceImpl(node), new PropertyImpl(generalIRI + "where"), new ResourceImpl(generalIRI + "BlueRoom "));

				csparqlAPI.feedStream("http://streamreasoning.org/streams/fb", m);	
				
			} catch (InterruptedException e) {
				logger.error("Error while launching the sleep operation", e);
			} catch (StreamErrorException e) {
				logger.error("StreamErrorException Occurred", e);
			} catch (ServerErrorException e) {
				logger.error("ServerErrorException Occurred", e);
			}


		}
	}

	public void stopStream(){
		keepRunning = false;
	}

}
