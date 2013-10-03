package polimi.deib.rsp_service4csparql_client_example.streamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class Four_Violations_DemoStreamer implements Runnable {

	private Csparql_Remote_API csparqlAPI;
	private String streamName;
	private long sleepTime;
	private String generalIRI;

	private Logger logger = LoggerFactory.getLogger(Four_Violations_DemoStreamer.class.getName());

	public Four_Violations_DemoStreamer(Csparql_Remote_API csparqlAPI, String streamName, long sleepTime, String generalIRI) {
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
			
			csparqlAPI.feedStream(streamName, m);

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("Error while launching the sleep operation", e);
			}

		}
	}

}
