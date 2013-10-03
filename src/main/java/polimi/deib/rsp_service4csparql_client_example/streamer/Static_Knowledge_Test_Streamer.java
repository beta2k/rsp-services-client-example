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

import polimi.deib.csparql_rest_api.Csparql_Remote_API;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

public class Static_Knowledge_Test_Streamer implements Runnable {

	/** The logger. */
	protected final Logger logger = LoggerFactory
			.getLogger(Static_Knowledge_Test_Streamer.class);	

	private boolean keepRunning = false;
	private String quadrupleIRI = "http://streamreasoning.org#";
	
	private Csparql_Remote_API csparqlAPI;
	private String streamName;

	public Static_Knowledge_Test_Streamer(Csparql_Remote_API csparqlAPI, String streamName) {
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
			
			Statement s = new StatementImpl(new ResourceImpl(quadrupleIRI+"w" + randomGenerator.nextInt(1000)), new PropertyImpl(quadrupleIRI+"isIn"), new ResourceImpl(quadrupleIRI+"r1"));
			m.add(s);

			s = new StatementImpl(new ResourceImpl(quadrupleIRI+"w" + randomGenerator.nextInt(1000)), new PropertyImpl(quadrupleIRI+"isIn"), new ResourceImpl(quadrupleIRI+"r2"));
			m.add(s);
			
			s = new StatementImpl(new ResourceImpl(quadrupleIRI+"w" + randomGenerator.nextInt(1000)), new PropertyImpl(quadrupleIRI+"isIn"), new ResourceImpl(quadrupleIRI+"r3"));
			m.add(s);
			
			s = new StatementImpl(new ResourceImpl(quadrupleIRI+"w" + randomGenerator.nextInt(1000)), new PropertyImpl(quadrupleIRI+"isIn"), new ResourceImpl(quadrupleIRI+"r4"));
			m.add(s);

			csparqlAPI.feedStream(streamName, m);

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
