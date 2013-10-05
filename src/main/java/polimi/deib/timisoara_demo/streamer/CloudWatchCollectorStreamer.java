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
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;
import polimi.deib.rsp_service4csparql_client_example.Client_Server;
import polimi.deib.timisoara_demo.ontology.MC;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;

public class CloudWatchCollectorStreamer implements Runnable {

	private String csparqlServerAddress;
	private Csparql_Remote_API csparqlAPI;
	private long sleepTime;
	private String streamIRI;
	private String datumBaseIRI;
	
	
	private String infrastructureIRI;
	private String platformIRI;
	private double cpuMeanValue;

	private Logger logger = LoggerFactory.getLogger(CloudWatchCollectorStreamer.class.getName());
	private String softwareIRI;

	public CloudWatchCollectorStreamer(String csparqlServerAddress, String streamIRI, long sleepTime, String infrastructureIRI, String platformIRI, 
			String softwareIRI, double cpuMeanValue) {
		super();
		this.csparqlServerAddress = csparqlServerAddress;
		csparqlAPI = new Csparql_Remote_API(csparqlServerAddress);
		this.sleepTime = sleepTime;
		this.streamIRI = streamIRI;
		this.datumBaseIRI = streamIRI + "/" + MC.MonitoringDatum.getLocalName() + "#";
		this.cpuMeanValue = cpuMeanValue;
		this.infrastructureIRI = infrastructureIRI;
		this.platformIRI = platformIRI;
		this.softwareIRI = softwareIRI;
	}

	public void run() {

		Model m;


		Random randomGenerator = new Random();
		
		while(true){
			
			double cpuValue = cpuMeanValue+0.05*(2*randomGenerator.nextDouble()-1);
			
			//CPU UTILIZATION
			m = ModelFactory.createDefaultModel();
			m.createResource(datumBaseIRI + Client_Server.nextID())
				.addProperty(RDF.type, MC.MonitoringDatum)
				.addProperty(MC.hasMetric, MC.CPUUtilization)
				.addProperty(MC.isAbout, m.createResource(infrastructureIRI)
							.addProperty(RDF.type, MC.Infrastructure)) // .addProperty(RDF.type, MC.ec2_large_machine);
				.addProperty(MC.hasValue, m.createTypedLiteral(cpuValue, XSDDatatype.XSDdouble));
			if (platformIRI != null) {
				m.createResource(platformIRI) // .addProperty(RDF.type, MC.MySQL);
					.addProperty(RDF.type, MC.Platform)
					.addProperty(MC.runsOn, m.createResource(infrastructureIRI));
			}
			if (softwareIRI != null) {
				m.createResource(softwareIRI) // .addProperty(RDF.type, MC.MySQL);
					.addProperty(RDF.type, MC.Software)
					.addProperty(MC.runsOn, m.createResource(infrastructureIRI));
				if (platformIRI != null) 
					m.createResource(softwareIRI).addProperty(MC.runsOn, m.createResource(platformIRI));
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
