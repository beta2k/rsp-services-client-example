package polimi.deib.rsp_service4csparql_client_example.streamer;

import java.util.Random;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;

public class MultiThreadStreamer implements Runnable {

	private Csparql_Remote_API csparqlAPI;
	private String streamName;
	private String generalIRI;

//	private Logger logger = LoggerFactory.getLogger(MultiThreadStreamer.class.getName());

	public MultiThreadStreamer(Csparql_Remote_API csparqlAPI, String streamName, String generalIRI) {
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
			
			csparqlAPI.feedStream(streamName, m);
		}
	}

}
