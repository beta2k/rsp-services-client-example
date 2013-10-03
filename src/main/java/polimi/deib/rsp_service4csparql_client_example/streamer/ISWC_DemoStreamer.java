package polimi.deib.rsp_service4csparql_client_example.streamer;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

import polimi.deib.csparql_rest_api.Csparql_Remote_API;

public class ISWC_DemoStreamer implements Runnable {

	private Csparql_Remote_API csparqlAPI;
	private String streamName;
	private long sleepTime;
	private String generalIRI;

	private Logger logger = LoggerFactory.getLogger(ISWC_DemoStreamer.class.getName());

	public ISWC_DemoStreamer(Csparql_Remote_API csparqlAPI, String streamName, long sleepTime, String generalIRI) {
		super();
		this.csparqlAPI = csparqlAPI;
		this.streamName = streamName;
		this.sleepTime = sleepTime;
		this.generalIRI = generalIRI;
	}

	public void run() {

		Model m;

		int i = 1;

		Random randomGenerator = new Random();
		while(true){
			
			m = ModelFactory.createDefaultModel();
			m.add(new ResourceImpl(generalIRI + "datum" + i), new PropertyImpl(generalIRI + "hasMonitoredMetric"), new ResourceImpl(generalIRI + "responseTime"));
			m.add(new ResourceImpl(generalIRI + "datum" + i), new PropertyImpl(generalIRI + "isAbout"), new ResourceImpl(generalIRI + "resource" + i));
			m.add(new ResourceImpl(generalIRI + "datum" + i), new PropertyImpl(generalIRI + "hasValue"), m.createTypedLiteral(randomGenerator.nextInt(10000), XSDDatatype.XSDinteger));
			m.add(new ResourceImpl(generalIRI + "datum" + i), new PropertyImpl(generalIRI + "isProcessedBy"), new ResourceImpl(generalIRI + "machine" + i));
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
