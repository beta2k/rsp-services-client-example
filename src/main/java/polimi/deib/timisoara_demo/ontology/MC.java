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
package polimi.deib.timisoara_demo.ontology;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class MC {

	protected static final String uri = "http://www.modaclouds.eu/ontology#";

	public static String getURI() {
		return uri;
	}

	protected static final Property property(String local) {
		return ResourceFactory.createProperty(uri, local);
	}

	protected static final Resource resource(String local) {
		return ResourceFactory.createResource(uri + local);
	}

	/** Core Ontology **/
	
	//Resources
	public static final Resource MonitoringDatum = resource("MonitoringDatum");
	public static final Resource Event = resource("Event");
	public static final Resource Compute = resource("Compute");
	public static final Resource Infrastructure = resource("Infrastructure");
	public static final Resource Platform = resource("Platform");
	public static final Resource Software = resource("Software");
	public static final Resource Operation = resource("Operation");
	public static final Resource Request = resource("Request");
	
	//Properties
	public static final Property hasMetric = property("hasMetric");
	public static final Property isAbout = property("isAbout");
	public static final Property hasValue = property("hasValue");
	public static final Property runsOn = property("runsOn");
	public static final Property asksFor = property("asksFor");
	public static final Property isProcessedBy = property("isProcessedBy");
	public static final Property isProvidedBy = property("isProvidedBy");

	
	/** Extension **/
	
	//Metrics:
	public static final Resource CPUUtilization = resource("CPUUtilization");
	public static final Resource ResponseCode = resource("ResponseCode");;
	public static final Resource AbortedConnectionEvent = resource("AbortedConnectionEvent");
	public static final Resource AttemptedConnectionEvent = resource("AttemptedConnectionEvent");
	public static final Resource SuccessfulRequestEvent = resource("SuccessfulRequestEvent");
	public static final Resource ResponseTime = resource("ResponseTime");
	public static final Resource AbortedConnections = resource("AbortedConnections");
	public static final Resource AttemptedConnections = resource("AttemptedConnections");
	public static final Resource SuccessfulRequests = resource("SuccessfulRequests");
	
	
	public static final Resource AmazonBackEndVM = resource("AmazonBackendVM");
	public static final Resource FlexiBackEndVM = resource("FlexiBackEndVM");
	public static final Resource MySQL = resource("MySQL");
	public static final Resource Login = resource("Login");
	public static final Resource Logout = resource("Logout");
	public static final Resource Checkout = resource("Checkout");
	public static final Resource OFBiz = resource("OFBiz");
	public static final Resource AmazonFrontEndVM = resource("AmazonFrontEndVM");
	public static final Resource FlexiFrontEndVM = resource("FlexiFrontEndVM");
	public static final Resource JVM = resource("Resource");
	
	
	
	
	
	
	
}
