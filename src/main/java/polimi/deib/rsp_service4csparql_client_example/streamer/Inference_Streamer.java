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
/*
 * @(#)TestGenerator.java   1.0   18/set/2009
 *
 * Copyright 2009-2009 Politecnico di Milano. All Rights Reserved.
 *
 * This software is the proprietary information of Politecnico di Milano.
 * Use is subject to license terms.
 *
 * @(#) $Id$
 */


import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import polimi.deib.csparql_rest_api.exception.ServerErrorException;
import polimi.deib.csparql_rest_api.exception.StreamErrorException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

public class Inference_Streamer implements Runnable {

	/** The logger. */
	protected final Logger logger = LoggerFactory
			.getLogger(Inference_Streamer.class);	

	private boolean keepRunning = false;
	private String quadrupleIRI = "http://streamreasoning.org#";

	private RSP_services_csparql_API csparqlAPI;
	private String streamName;

	public Inference_Streamer(RSP_services_csparql_API csparqlAPI, String streamName) {
		super();
		this.csparqlAPI = csparqlAPI;
		this.streamName = streamName;
	}

	public void pleaseStop() {
		keepRunning = false; 
	}

	@Override
	public void run() {
		keepRunning = true;

		Random randomGenerator = new Random();
		Model m;

		while (keepRunning) {

			m = ModelFactory.createDefaultModel();

			int x = randomGenerator.nextInt(1000);
			System.out.println("Streamed " + x + " at " + System.currentTimeMillis()); 

			Statement s = new StatementImpl(new ResourceImpl(quadrupleIRI+"t" + x), new PropertyImpl(quadrupleIRI+"mum"), new ResourceImpl(quadrupleIRI+"m"));
			m.add(s);

			try{
				csparqlAPI.feedStream(streamName, m);
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (StreamErrorException e) {
				logger.error("StreamErrorException Occurred", e);
			} catch (ServerErrorException e) {
				logger.error("ServerErrorException Occurred", e);
			}
		}
	}
}
