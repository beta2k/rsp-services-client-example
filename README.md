Example of client for C-SPARQL implementation of RDF stream Processors RESTful Interfaces
===================

To configure the Example of client C-SPARQL implementation of RDF stream Processors RESTful Interfaces please open the setup.properties file

Configuration:
client_server.port.port : configure the port of the server

Execute the Server:
Import project in eclipse as a maven project.
Open Client_Server class in polimi.deib.rsp_service4csparql_client_example package.

The class offers a list of little example of interaction between client server and rsp-service4csparql rest server:
- SINGLE_SELECT_QUERY_SINGLE_OBSERVER : presents a little example of select query registration and results visualization using a simple observer
- SINGLE_CONSTRUCT_QUERY_SINGLE_OBSERVER : presents a little example of construct query registration and results visualization using a simple observer
- QUERY_CHAIN : presents an example of select query chain registration, the second query uses the data produced by the first registered query
- MODACLOUDS_FIRST_DEMO : presents an example using modaclouds data
- SINGLE_CONSTRUCT_QUERY_SINGLE_OBSERVER_HI_PRESSURE_TEST : presents an example of construct query registration and results visualization using a simple observer. The feed is feeded by multiple hi throughput theead
- SPARQL_UDATE : presents an example of update operation on static knowledge base, using SPARQL UPDATE query language (http://www.w3.org/TR/2013/REC-sparql11-update-20130321/)
- RDFS_INFERENCE : presents an example of RDFS inference usage (please set RDFS inference in the server)
- ISWC_TUTORIAL_DEMO_TRANSITIVE_INFERENCE : presents an example of transitive inference usage. (please set transitive inference in the server)

For ISWC_TUTORIAL_DEMO_TRANSITIVE_INFERENCE example the stream feed operation must be done by using a rest client (https://addons.mozilla.org/it/firefox/addon/restclient/).
To feed the stream http://ex.org/roomstream (the name of the stream must be encoded using an HTTP encoder):

POST http://localhost:8175/streams/http%3A%2F%2Fex.org%2Froomstream

Data to put in the body of the POST:

{ 
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA0" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#where" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#roomA"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA1" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA0"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA2" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA1"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA3" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA2"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA4" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#postA3"
    }
     ]
  }
,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post0" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#where" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#roomB"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post1" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post0"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post2" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post1"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post3" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post2"
    }
     ]
  }
   ,
  "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post4" : { 
    "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#discusses" : [ { 
      "type" : "uri" ,
      "value" : "http://www.streamreasoning.org/ontologies/2013/9/hands-on-ontology.owl#post3"
    }
     ]
  }
}
