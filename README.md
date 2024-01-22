# Accenture Exercise
___
## Overview
This project addresses the Accenture programming exercise by leveraging the [Rest Countries API](https://restcountries.com). It provides insights into countries' data with two key functionalities:
* Displaying a sorted list of all countries by their population in descending order.
* Identifying the country in Asia with the most bordering countries from different regions.
## Solution
### Project structure
The solution is structured into a multi-module Maven project comprising:

* `api`: A Spring Boot application offering RESTful endpoints to access the required information.
* `console`: A simple Maven-based console application for displaying the required information in a command-line interface.
* `common`: A shared module that houses common logic and resources utilized by both the api and console modules.

The solution consists of two different ways to display the answers
* Rest Solution: Access via RESTful endpoints, suitable for integration into web services or applications.
* Console Solution: Direct interaction through a command-line interface, ideal for standalone usage.

The logic mostly resides in `AbstractCountriesService` and is shared between api and console. the modules define how data is pulled from the restcountries api.
## How to run
* Clone the repository
```shell
git clone git@github.com:BishoDroid/countries-exercise.git
```
**From IDE**
* Import the project into your preferred IDE
* Make sure to enable [`Annotation Processing`](https://www.jetbrains.com/help/idea/annotation-processors-support.html) if you are using IntelliJ
* Run Maven compile 
```shell
mvn clean compile
```
* For the `console` app, run `ConsoleApplication` and it will output  the required exercise information into the console.
* For the `api` app, run `ApiApplication` and go to `http://localhost:8080/swagger-ui/index.html` where you will find the documented APIs and how to use them.

**From Jar**
* Go to where you have imported the project
* Run Maven package 
```shell
mvn clean package
```
* For the `console` app, run
```shell
java -jar ./console/target/console-app.jar
```
* For the `api` app, run
```shell
java -jar ./api/target/api-app.jar
```
  
## Tools used
* For API
  * `spring-boot`: to setup a simple rest api to expose the endpoints of the solution
  * `spring-doc`: for documenting the api endpoints and setting up swagger
* For Console
  * `okHttp`: To make http requests to get the required resources to solve the exercise.
  * `slf4j-log4j12 and jcabi`: for formatted logging in the console.
  
## Important notes
* The solution could be as simple as a console program that calls the restcountries api endpoints and prints out the results.
* I was not sure exactly what format was the solution needed to be in hence the `console` and `api` solutions to give you the options to choose.
* I could also do without `log4j` and `jcabi` and just use stdout in `console` for simplicity if needs be.
* Even though for the second part of the question "country in Asia with the most bordering countries from different regions", it is specific enough, I parameterized the solution in case the answer is also needed for another region (hope that's ok).