#!/bin/bash
printf build maven project
mvn clean install
printf run project
java -jar target/offerCreditApp-1.0.jar