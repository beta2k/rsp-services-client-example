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
package polimi.deib.rsp_service4csparql_client_example.streamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.csparql_rest_api.exception.StreamErrorException;

public class BaseStreamer implements Runnable {

	private Csparql_Remote_API csparqlAPI;
	private String streamName;
	private long sleepTime;
	private String generalIRI;
	private boolean keepRunning = true;

	private Logger logger = LoggerFactory.getLogger(BaseStreamer.class.getName());

	public BaseStreamer(Csparql_Remote_API csparqlAPI, String streamName, long sleepTime, String generalIRI) {
		super();
		this.csparqlAPI = csparqlAPI;
		this.streamName = streamName;
		this.sleepTime = sleepTime;
		this.generalIRI = generalIRI;
	}

	public void run() {

		Model m;

		int i = 1;

		while(keepRunning){

			m = ModelFactory.createDefaultModel();
			m.add(new ResourceImpl(generalIRI + "s" + i), new PropertyImpl(generalIRI + "p" + i), new ResourceImpl(generalIRI + "o" + i));
			i++;
			m.add(new ResourceImpl(generalIRI + "s" + i), new PropertyImpl(generalIRI + "p" + i), new ResourceImpl(generalIRI + "o" + i));
			i++;
			m.add(new ResourceImpl(generalIRI + "s" + i), new PropertyImpl(generalIRI + "p" + i), new ResourceImpl(generalIRI + "o" + i));
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
	
	public void stopStream(){
		keepRunning = false;
	}

}
