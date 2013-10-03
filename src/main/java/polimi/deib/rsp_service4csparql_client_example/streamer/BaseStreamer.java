package polimi.deib.rsp_service4csparql_client_example.streamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;

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

			csparqlAPI.feedStream(streamName, m);
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("Error while launching the sleep operation", e);
			}


		}
	}
	
	public void stopStream(){
		keepRunning = false;
	}

}
