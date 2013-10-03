package generalJenaTest;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;

public class QueryValidator {

	public static void main(String[] args) {
		
		String querytext = 	"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX mc: <http://ex.org> " +
				"PREFIX mr: <http://ex.org/MonitoringRule#> " +
				"CONSTRUCT { [] rdf:type mc:ViolationEvent ;" +
				"mc:isGeneratedBy mr:ResponseTimeViolation . }" +
				"WHERE { " +
					"SELECT ?machine (AVG(?value) AS ?aggregation) " +
					"WHERE { " +
						"?datum mc:hasMonitoredMetric ?metric . " +
						"?datum mc:isAbout ?resource . " +
						"?datum mc:hasValue ?value . " +
						"?datum mc:isProcessedBy ?machine . " + 
					"} " +
					"GROUP BY (?machine)" +
					"HAVING (?aggregation > \"3000\"^^xsd:integer) " +
				"}";
		
		Query q = QueryFactory.create(querytext, Syntax.syntaxSPARQL_11);

	}

}
