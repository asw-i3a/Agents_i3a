# I3A Agents Service
| | **Status** |
|---|:----|
| **circleCI** | [![CircleCI](https://circleci.com/gh/asw-i3a/agents-service/tree/master.svg?style=svg)](https://circleci.com/gh/asw-i3a/agents-service/tree/master)
| **code coverage** | [![codecov](https://codecov.io/gh/asw-i3a/agents-service/branch/master/graph/badge.svg)](https://codecov.io/gh/asw-i3a/agents-service)
| **code quality** | [![Codacy Badge](https://api.codacy.com/project/badge/Grade/e1e90c5a89fd4da6908296545e952c81)](https://www.codacy.com/app/colunga91/agents-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=asw-i3a/agents-service&amp;utm_campaign=Badge_Grade)
| **latest build** |[![Docker Badge](https://img.shields.io/badge/docker%20image-latest-blue.svg)](https://hub.docker.com/r/incisystem/agents_service/)

**Welcome to our agents service**

This micro-servide forms part of platform called GestUsers, if you don't know about it, we encourage you to see this other [repo](https://github.com/asw-i3a/project-documentation) first.

Here your will find the source code of a micro-service dedicated to write / read operations in a database of agents. _An agent is is a person, sensor or entity that can submit incidents in to the system._

**API Documentation:** https://github.com/asw-i3a/agents-service/wiki/API-Documentation

## Authors

- Herminio García González (@herminiogg)
- Jose Emilio Labra Gayo (@labra)
- Jorge Zapatero Sánchez (@JorgeZapa)
- Damián Rubio Cuervo (@DamianRubio)
- Antonio Nicolás Rivero Gómez (@Lan5432)

### 2018 - Maintainers
- [Guillermo Facundo Colunga](https://github.com/thewilly)
- [Carlos García Hernández](https://github.com/CarlosGarciaHdez)
- [Victor Suárez Fernández](https://github.com/ByBordex)

## Contributing to this service
Contributions to the project are welcomed and encouraged! Please see the [Contributing guide](/CONTRIBUTING.md).

## Getting Started
These instructions give the most direct path to work with the project.

### System Requirements
As the project is developed in java macOS, Windows and Linux distributions are natively supported. Of course you will need the latest JDK available. Also, depending on where are you going to run the database, you will need internet connection or MongoDB installed and running on your machine.

#### Java Development Kit (JDK)
A Java Development Kit (JDK) is a program development environment for writing Java applets and applications. It consists of a runtime environment that "sits on top" of the operating system layer as well as the tools and programming that developers need to compile, debug, and run applets and applications written in the Java programming language.

If you do not has the latest stable version download you can download it [here](http://www.oracle.com/technetwork/java/javase/downloads).

#### MongoDB
This project uses MongoDB as the database. You can check how to use it on [MongoDB install](https://github.com/Arquisoft/participants_i2b/wiki/MongoDB). By defatult a dummy server is up and running, it´s configured at the file `applications.properties`. Change this configuration as needed, should not interfeer with the module itself.

#### Jasypt
This project uses Jasypt to encrypt the passwords. You don't need to download it as far as its dependency its imported from maven central, but you can check it [here](http://www.jasypt.org/).

### Getting Sources
First create a directory for all of the project sources:
```
mkdir agents_service
cd agents_service
```
**Cloning repository**
```
git clone https://github.com/asw-i3a/agents-service.git
```

### Building the service
As the project was created on [eclipse](https://www.eclipse.org) this is the best way to build and run the sources. The steps are the following:
1. Open Eclipse IDE.
2. Select import existing Maven Project.
3. Go to `src/main/java/main/Application.java`.
4. Right click over the class and Run as Java Application.

Up to this point the module should be up and running in the address [localhost:8080](http://localhost:8080).

### Working with this service

#### Running the service
To run the module you need to set the repository as working directory and run the following statement
```bash
mvn spring-boot:run
```

#### REST requests
In order to use the user credentials to obtain your data, you can send a POST request to [localhost:8080/auth](http://localhost:8080/user). The
data in this request can come as:
##### JSON
```json
{"login":"agent-identifier", "password":"agent-password", "kind":"agent-kind-as-integer-code"}
```

##### XML
```xml
<data>
 <login>agent-identifier</login>
 <password>agent-password</password>
 <kind>agent-kind-as-integer-code</kind>
</data>
```
### Testing the service
To run the already existing tests of the module you can choose by running as JUnit tests, from the IDE. Or as the implements Maven you can run Maven tests task as `mvn test`. If the database is up and running everything should go fine and smooth. No data in the database is needed to run them. But if you want to test the
user interface manually you'll have to introduce this document:
```json
{
    "_id" : "58a8670df086e81dc034d7fc",
    "name" : "Clara Oswin Oswald",
    "location" : "10N10W",
    "email" : "oswald@tardis.universe",
    "kindCode" : "1",
    "id" : "45170000A",
    "password" : "khZZwjdhWwVbMdmOvz9eqBfKR1N6A+CdFBDM9c1dQduUnGewQyPRlBxB4Q6wT7Cq"
}
```

And as data use:
 - login: 45170000A
 - password: 4[[j[frVCUMJ>hU
 - Kind of agent: Person

 If you have the data and the database running and the application still reports a 404 Not Found when it shouldn't
 try deleting the document and adding it again.

As an alternative, you can perform tests on the REST service by executing the next function in the command line:

```bash
curl -H "Content-Type: application/json" -X POST -d '{"login":"45170000A","password":"4[[j[frVCUMJ>hU", "kind":1}' http://localhost:8080/auth
```

 Take into account that the parameters passed in the function are the same as the previous JSON file, so they have to be synchronised.
