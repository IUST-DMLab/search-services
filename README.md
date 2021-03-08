FarsBase Semantic Search Services
==========

### Introduction
This repository contains the Spring-based web service for the FarsBase Semantic Search.

search-services is a web service that translates a JSON request and runs [searcher](https://github.com/IUST-DMLab/searcher) (the core component of Semantic Search).

## Starting the Semantic Search User Interface
Tu run the package do:

    mvn clean package
    java -jar target/services-0.4.4.jar

The service will then be available under localhost:1920/services.


## License
The source code of this repo is published under the [Apache License Version 2.0](https://github.com/AKSW/jena-sparql-api/blob/master/LICENSE).

