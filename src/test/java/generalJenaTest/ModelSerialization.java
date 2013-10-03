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
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class ModelSerialization {

	public static void main(String[] args) {

		String baseURI = "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#";
		Model m = ModelFactory.createDefaultModel();

//		m.add(new ResourceImpl(baseURI + "po"), new PropertyImpl(baseURI + "where"), new ResourceImpl(baseURI + "r"));
//		m.add(new ResourceImpl(baseURI + "po1"), new PropertyImpl(baseURI + "discusses"), new ResourceImpl(baseURI + "po"));
//		m.add(new ResourceImpl(baseURI + "po2"), new PropertyImpl(baseURI + "discusses"), new ResourceImpl(baseURI + "po1"));
//		m.add(new ResourceImpl(baseURI + "po3"), new PropertyImpl(baseURI + "discusses"), new ResourceImpl(baseURI + "po2"));
//		m.add(new ResourceImpl(baseURI + "po4"), new PropertyImpl(baseURI + "discusses"), new ResourceImpl(baseURI + "po3"));

		m.add(new ResourceImpl(baseURI + "postA0"), new PropertyImpl(baseURI + "where"), new ResourceImpl(baseURI + "roomA"));

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
