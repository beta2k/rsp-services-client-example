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
 * 
 * Acknowledgements:
 * 
 * This work was partially supported by the European project LarKC (FP7-215535) 
 * and by the European project MODAClouds (FP7-318484)
 ******************************************************************************/
package generalJenaTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class ModelSerialization {

	public static void main(String[] args) {

		String baseURI = "http://streamreasoning.org/ontologies/sr4ld2013-onto#";
		Model m = ModelFactory.createDefaultModel();

		AnonId a = new AnonId();
		m.add(new ResourceImpl(baseURI + "Bob"), new PropertyImpl(baseURI + "posts"), new ResourceImpl(a));
		m.add(new ResourceImpl(a), new PropertyImpl(baseURI + "who"), new ResourceImpl(baseURI + "Bob"));
		m.add(new ResourceImpl(a), new PropertyImpl(baseURI + "where"), new ResourceImpl(baseURI + "BlueRoom"));
//		m.add(new ResourceImpl(baseURI + "po3"), new PropertyImpl(baseURI + "discusses"), new ResourceImpl(baseURI + "po2"));
//		m.add(new ResourceImpl(baseURI + "po4"), new PropertyImpl(baseURI + "discusses"), new ResourceImpl(baseURI + "po3"));

//		m.add(new ResourceImpl(baseURI + "Alice"), RDF.type, new ResourceImpl(baseURI + "Person"));
//		m.add(new ResourceImpl(baseURI + "Bob"), RDF.type, new ResourceImpl(baseURI + "Person"));
//		m.add(new ResourceImpl(baseURI + "Carl"), RDF.type, new ResourceImpl(baseURI + "Person"));
//		m.add(new ResourceImpl(baseURI + "David"), RDF.type, new ResourceImpl(baseURI + "Person"));
//		m.add(new ResourceImpl(baseURI + "Elen"), RDF.type, new ResourceImpl(baseURI + "Person"));
//		m.add(new ResourceImpl(baseURI + "RedRoom"), RDF.type, new ResourceImpl(baseURI + "Room"));
//		m.add(new ResourceImpl(baseURI + "BlueRoom"), RDF.type, new ResourceImpl(baseURI + "Room"));
//		m.add(new ResourceImpl(baseURI + "RedRoom"), new PropertyImpl(baseURI + "isConnectedTo"), new ResourceImpl(baseURI + "BlueRoom"));
//		m.add(new ResourceImpl(baseURI + "RedSensor"), RDF.type, new ResourceImpl(baseURI + "Sensor"));
//		m.add(new ResourceImpl(baseURI + "BlueSensor"), RDF.type, new ResourceImpl(baseURI + "Sensor"));

		StringWriter w = new StringWriter();

		m.write(w,"RDF/JSON");
		System.out.println();
		m.write(w,"RDF/XML");
		System.out.println();
		m.write(w,"TURTLE");
		System.out.println();
		m.write(w,"N-TRIPLE");

		System.out.println(w.toString());

//		String qString = "SELECT * " +
//				"WHERE { " +
//				"?s ?p ?o " +
//				"}";
//
//		Query q = QueryFactory.create(qString, Syntax.syntaxSPARQL_11);
//		QueryExecution qexec = QueryExecutionFactory.create(q,m);
//
//		ResultSet rs = qexec.execSelect();
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ResultSetFormatter.outputAsJSON(bos, rs);
//
//		System.out.println(bos.toString());
//
//		Model model = ModelFactory.createDefaultModel();
//		try {
//			model.read(new ByteArrayInputStream(w.toString().getBytes("UTF-8")),null,"RDF/JSON");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println();
//		System.out.println("------------------");
//		System.out.println();
//
//		model.write(System.out);


	}

}
