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
package it.polimi.deib.rsp_service4csparql_client_example.streamer;

import it.polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import it.polimi.deib.csparql_rest_api.exception.ServerErrorException;
import it.polimi.deib.csparql_rest_api.exception.StreamErrorException;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class MultiThreadStreamer implements Runnable {

	private RSP_services_csparql_API csparqlAPI;
	private String streamName;
	private String generalIRI;

	private Logger logger = LoggerFactory.getLogger(MultiThreadStreamer.class.getName());

	//	private Logger logger = LoggerFactory.getLogger(MultiThreadStreamer.class.getName());

	public MultiThreadStreamer(RSP_services_csparql_API csparqlAPI, String streamName, String generalIRI) {
		super();
		this.csparqlAPI = csparqlAPI;
		this.streamName = streamName;
		this.generalIRI = generalIRI;
	}

	public void run() {

		Model m;
		Random rnd = new Random();
		int i = 0;

		while(true){

			i = rnd.nextInt(99999999);
			m = ModelFactory.createDefaultModel();
			m.add(new ResourceImpl(generalIRI + "s" + i), new PropertyImpl(generalIRI + "p" + i), new ResourceImpl(generalIRI + "o" + i));

			try{
				csparqlAPI.feedStream(streamName, m);
			} catch (StreamErrorException e) {
				logger.error("StreamErrorException Occurred", e);
			} catch (ServerErrorException e) {
				logger.error("ServerErrorException Occurred", e);
			}
		}
	}

}
