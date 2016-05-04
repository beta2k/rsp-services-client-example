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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class Percentile_Streamer implements Runnable {

	private RSP_services_csparql_API csparqlAPI;
	private String streamName;
	private long sleepTime;
	private String generalIRI;

	private Logger logger = LoggerFactory.getLogger(Percentile_Streamer.class.getName());

	public Percentile_Streamer(RSP_services_csparql_API csparqlAPI, String streamName, long sleepTime, String generalIRI) {
		super();
		this.csparqlAPI = csparqlAPI;
		this.streamName = streamName;
		this.sleepTime = sleepTime;
		this.generalIRI = generalIRI;
	}

	public void run() {

		Model m;
		
		Property response_time = new PropertyImpl(generalIRI + "response_time");
		
		while(true){
			
			m = ModelFactory.createDefaultModel();

//			m.read(FileManager.get().open("/Users/baldo/Desktop/test.rdf"),null);

			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(115, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(95, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(77, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(110, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(80, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(127, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm1"), response_time, m.createTypedLiteral(22, XSDDatatype.XSDfloat));
			
			m.add(new ResourceImpl(generalIRI + "vm2"), response_time, m.createTypedLiteral(95, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm2"), response_time, m.createTypedLiteral(23, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm2"), response_time, m.createTypedLiteral(55, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm2"), response_time, m.createTypedLiteral(115, XSDDatatype.XSDfloat));
			m.add(new ResourceImpl(generalIRI + "vm2"), response_time, m.createTypedLiteral(80, XSDDatatype.XSDfloat));
						
			
			try{
				csparqlAPI.feedStream(streamName, m);
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

}
