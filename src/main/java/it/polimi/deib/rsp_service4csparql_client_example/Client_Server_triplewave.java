package it.polimi.deib.rsp_service4csparql_client_example;

import it.polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import it.polimi.deib.csparql_rest_api.exception.ServerErrorException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Hashtable;

public class Client_Server_triplewave{

    private static Hashtable<String, String> queryProxyIdTable = new Hashtable<String, String>();
    private static Logger logger = LoggerFactory.getLogger(Client_Server_triplewave.class.getName());

    private static int ID = 0;


    public static void main(String[] args) throws Exception{

        //Set to false if you need client server only for writing results
        boolean startExample = true;

        String propertiesFilePath;
        String log4jConf;

        if(args.length >0 && args[0] != null)
            propertiesFilePath = args[0];
        else
            propertiesFilePath = Client_Server_triplewave.class.getResource( "/config.properties" ).getFile();
        if(args.length >0 && args[1] != null)
            log4jConf = args[1];
        else
            log4jConf = Client_Server_triplewave.class.getResource( "/log4j.properties" ).getFile();

        if(log4jConf.startsWith("http://"))
            PropertyConfigurator.configure(new URL(log4jConf));
        else
            PropertyConfigurator.configure(log4jConf);

        PropertiesConfiguration config = new PropertiesConfiguration(propertiesFilePath);

        String key = config.getString("rsp.client.example");
        String csparqlServerAddress = config.getString("rsp.server.address");

        String query;

        String inputstreamName = null;

        RSP_services_csparql_API csparqlAPI = new RSP_services_csparql_API(csparqlServerAddress);

        //http://localhost:8114/TripleWave-transform/sgraph
        //http://localhost:8114/TripleWave-replay/sgraph

        switch(key){

            case "TEST":
                try {

                    csparqlAPI.registerStream("str1", "http://localhost:8114/TripleWave-transform/sgraph");

                    query = "REGISTER QUERY q1 AS " +
                            "PREFIX prov:<http://www.w3.org/ns/prov#> " +
                            "PREFIX sc:<https://schema.org/> " +
                            "SELECT ?agent (COUNT(?t) AS ?c) " +
                            "FROM STREAM <str1> [RANGE 15m STEP 1m] " +
                            "WHERE { " +
                            "?t sc:agent ?agent " +
                            "} " +
                            "GROUP BY ?agent " +
                            "ORDER BY desc(?c) " +
                            "LIMIT 15";

                    csparqlAPI.registerQuery("q1", query);
                    csparqlAPI.addObserver("q1", "localhost", 8081);

                } catch (ServerErrorException e) {
                    logger.error("rsp_server4csparql_server error", e);
                }

                break;

            case "TOPPAGE":
                try {

                    csparqlAPI.registerStream("wikiTransormationStream", "http://131.175.141.249/TripleWave-transform/sgraph");

                    query = "REGISTER QUERY topUpdateWiki AS " +
                            "PREFIX prov:<http://www.w3.org/ns/prov#> " +
                            "PREFIX sc:<https://schema.org/> " +
                            "SELECT ?obj (COUNT(?t) AS ?c) " +
                            "FROM STREAM <wikiTransormationStream> [RANGE 1m STEP 1m] " +
                            "WHERE { " +
                            "?t sc:object ?obj " +
                            "FILTER (!contains(str(?obj), \"Special:Log\")) " +
                            "} " +
                            "GROUP BY ?obj " +
                            "ORDER BY desc(?c) " +
                            "LIMIT 15";

                    csparqlAPI.registerQuery("topUpdateWiki", query);
                    csparqlAPI.addObserver("topUpdateWiki", "localhost", 8081);


                } catch (ServerErrorException e) {
                    logger.error("rsp_server4csparql_server error", e);
                }

                break;

            case "TOPAGENT":
                try {

                    csparqlAPI.registerStream("wikiTransormationStream", "http://131.175.141.249/TripleWave-transform/sgraph");

                    query = "REGISTER QUERY topUserWiki AS " +
                            "PREFIX prov:<http://www.w3.org/ns/prov#> " +
                            "PREFIX sc:<https://schema.org/> " +
                            "SELECT ?agent (COUNT(?t) AS ?c) " +
                            "FROM STREAM <wikiTransormationStream> [RANGE 1m STEP 1m] " +
                            "WHERE { " +
                            "?t sc:agent ?agent " +
                            "} " +
                            "GROUP BY ?agent " +
                            "ORDER BY desc(?c) " +
                            "LIMIT 15";

                    csparqlAPI.registerQuery("topUserWiki", query);
                    csparqlAPI.addObserver("topUserWiki", "localhost", 8081);

                } catch (ServerErrorException e) {
                    logger.error("rsp_server4csparql_server error", e);
                }

                break;

            case "AVGSENSOR":
                try {

                    csparqlAPI.registerStream("ssnSensor", "http://localhost:8114/TripleWave-replay/sgraph");

                    query = "REGISTER QUERY averageSensor AS  " +
                            "PREFIX om-owl:<http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#>  " +
                            "PREFIX weather: <http://knoesis.wright.edu/ssw/ont/weather.owl#>  " +
                            "SELECT ?obsProp (AVG(?value) AS ?v)  " +
                            "FROM STREAM <ssnSensor> [RANGE 15m STEP 1m]  " +
                            "WHERE {  " +
                            "?obs om-owl:observedProperty ?obsProp ; " +
                            "om-owl:result ?res .  " +
                            "?res om-owl:floatValue ?value  " +
                            "} " +
                            "GROUP BY ?obsProp " +
                            "ORDER BY desc(?v) " +
                            "LIMIT 15";

                    csparqlAPI.registerQuery("averageSensor", query);
                    csparqlAPI.addObserver("averageSensor", "localhost", 8081);

                } catch (ServerErrorException e) {
                    logger.error("rsp_server4csparql_server error", e);
                }

                break;

            default:
                System.exit(0);
                break;
        }

        try {
            Thread.sleep(1500000);
        } catch (InterruptedException e) {
            logger.error("Error while launching the sleep operation", e);
        }

        csparqlAPI.unregisterStream(inputstreamName);

    }
}
