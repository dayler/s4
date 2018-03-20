# Super Simple Scheduling System

This is an API of RestFul services that allow us manage Students and claesses. 

# Tecnoligies

The tecnologies that was used to build de project:
* apache-tomcat 8.5: It is one of the most popular web container.
* Jersey 2.25: Framework to build Restful web services. It is one of the most common frameworks, it is robust and easy to use.
* Spring 3: It has compatibility with Jersey, it is used for CDI (Context Dependency Injection).
* Juni 4.12: Unit test framework. 
* Mockito2.7.22: Mocking framework.

## Getting Started

Get the source code from: https://github.com/dayler/truextend-s4.git 

To compile: $ mvn clean install

### Prerequisites

* apache-tomcat 8.5 or higher.
* Java 8.X
* maven

### Installing

Get source code from https://github.com/dayler/truextend-s4.git 

$ git clone https://github.com/dayler/truextend-s4.git 

In the directory in which was clone the project (for example s4) execute the following command to compile.

$ mvn clean install

It will generate a target directory, in which you can find the deployable/runnable

<s4 root>/target/s4.war

To deploy it, just copy it in the "webapps" directory of your apache-tomcat installation. Be sure to have auto deploy enabled.

After to deploy you can get the WADL from: http://<IP>:<Port>/s4/ws/application.wadl

### Package Structure

In order to take a look in the source code, the project is organized in the following pakages.

* com.truextend.s4.bean: Contains all controllers for the app.
* com.truextend.s4.config: Configuration for Spring.
* com.truextend.s4.domain: POJO objects used throughout the app
* com.truextend.s4.exception: Custom exceptions and exceptions handlers.
* com.truextend.s4.ws: Contains the implementation of web services.

## Built With

* [Spring](https://spring.io) - Framework used for context dependecy injection.
* [Jersey](https://jersey.github.io) - Framework to develop RESTFul web services.
* [JUnit](http://junit.org/junit4/) - Unit test framework.
* [Maven](https://maven.apache.org/) - Dependency Management.
* [Mockito](http://site.mockito.org/) - Mocking framework for unit test in java.

## Authors

* **Ariel Dayler Salazar Hinojosa** - [dayler](https://github.com/dayler)
