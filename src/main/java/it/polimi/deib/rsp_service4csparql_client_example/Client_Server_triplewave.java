package it.polimi.deib.rsp_service4csparql_client_example;

import it.polimi.deib.csparql_rest_api.RSP_services_csparql_API;
import it.polimi.deib.csparql_rest_api.exception.ServerErrorException;
import it.polimi.deib.rsp_service4csparql_client_example.results_manipulator.ResultsManipulator;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Hashtable;

public class Client_Server_triplewave extends Application{

    private static Component component;
    private static Hashtable<String, String> queryProxyIdTable = new Hashtable<String, String>();
    private static Logger logger = LoggerFactory.getLogger(Client_Server_triplewave.class.getName());

    private static int ID = 0;


    public static void main(String[] args) throws Exception{

        //Set to false if you need client server only for writing results
        boolean startExample = true;

        String propertiesFilePath;

        PropertyConfigurator.configure(new URL("http://streamreasoning.org/configuration_files/rspCsparql/log4j.properties"));

        if(args.length > 0){
            propertiesFilePath = args[0];
        } else {
            propertiesFilePath = "http://streamreasoning.org/configuration_files/rspCsparql/setup.properties";
        }

        PropertiesConfiguration config = new PropertiesConfiguration(propertiesFilePath);

        String key = config.getString("rsp.client.example");
        String csparqlServerAddress = config.getString("rsp.server.address");

        String actual_client_address;
        int actual_client_port;

        component = new Component();
        component.getServers().add(Protocol.HTTP, config.getInt("rsp.client.port"));
        component.getClients().add(Protocol.FILE);

        Client_Server_triplewave csparqlServer = new Client_Server_triplewave();
        component.getDefaultHost().attach("", csparqlServer);

        component.start();

        actual_client_address = component.getServers().get(0).getAddress();
        if(actual_client_address == null)
            actual_client_address = "http://localhost";
        actual_client_port = component.getServers().get(0).getActualPort();

        String query;

        String inputstreamName = null;

        String queryURI;

        RSP_services_csparql_API csparqlAPI = new RSP_services_csparql_API(csparqlServerAddress);

        switch(key){

            case "QUERY1":
                try {

//				inputstreamName = generalIRI + "onlinefeeding";

                    csparqlAPI.registerStream("http://131.175.141.249/TripleWave-transform/sgraph");

                    query = "REGISTER QUERY topUpdateWiki AS " +
                            "PREFIX prov:<http://www.w3.org/ns/prov#> " +
                            "PREFIX sc:<https://schema.org/> " +
                            "SELECT ?obj (COUNT(?t) AS ?c) " +
                            "FROM STREAM <http://131.175.141.249/TripleWave-transform/sgraph> [RANGE 1m STEP 1m] " +
                            "WHERE { " +
                            "?t sc:object ?obj " +
                            "FILTER (!contains(str(?obj), \"Special:Log\")) " +
                            "} " +
                            "GROUP BY ?obj " +
                            "ORDER BY desc(?c) " +
                            "LIMIT 15";

                    queryURI = csparqlAPI.registerQuery("topUpdateWiki", query);
                    csparqlAPI.addObserver(queryURI, actual_client_address + ":" + actual_client_port + "/results");

                } catch (ServerErrorException e) {
                    logger.error("rsp_server4csparql_server error", e);
                }

                break;

            case "QUERY2":
                try {

                    csparqlAPI.registerStream("http://131.175.141.249/TripleWave-transform/sgraph");

                    query = "REGISTER QUERY topUserWiki AS " +
                            "PREFIX prov:<http://www.w3.org/ns/prov#> " +
                            "PREFIX sc:<https://schema.org/> " +
                            "SELECT ?agent (COUNT(?t) AS ?c) " +
                            "FROM STREAM <http://131.175.141.249/TripleWave-transform/sgraph> [RANGE 1m STEP 1m] " +
                            "WHERE { " +
                            "?t sc:agent ?agent " +
                            "} " +
                            "GROUP BY ?agent " +
                            "ORDER BY desc(?c) " +
                            "LIMIT 15";

                    queryURI = csparqlAPI.registerQuery("topUserWiki", query);
                    csparqlAPI.addObserver(queryURI, actual_client_address + ":" + actual_client_port + "/results");

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

    public Restlet createInboundRoot(){

        getContext().getAttributes().put("queryProxyIdTable", Client_Server_triplewave.queryProxyIdTable);

        Router router = new Router(getContext());
        router.setDefaultMatchingMode(Template.MODE_EQUALS);

        router.attach("/results", ResultsManipulator.class);

        return router;
    }

    public static int nextID() {
        return ID++;
    }

}
