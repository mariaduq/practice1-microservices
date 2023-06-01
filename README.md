# practice1-microservices

Project that provides an application consisting of four modules: client, server, worker and external service.

These modules communicate with each other as follows:
- External service: receives a text and returns it in uppercase.
- Client: will be able to request the execution of a task by invoking a REST API provided by the server.
- Server: implements a REST API with which clients can request tasks.
- Worker: serve task requests through an AMQP queue. To start the task, the text of the AMQP message is obtained and a call to external service is made.

## Getting Started 🚀

### Prerequisites

To run it, you need to have Docker installed locally. 

### Installing

First, you have to clone the repository in the desired directory with the following command:

```
$ git clone https://github.com/mariaduq/practice1-microservices.git
```
Once this is done, you have to install rabbitmq docker image with the next commands:

```
$ sudo docker run -d -p 15672:15672 -p 5672:5672 -p 5671:5671 --hostname my-rabbitmq --name my-rabbitmq-container rabbitmq
```
```
$ sudo docker exec my-rabbitmq-container rabbitmq-plugins enable rabbitmq_management
```
Now, you have to open browser in http://localhost:15672/ and login with user guest / guest.

Once you have logged in, create newTasks and tasksProgress queues (Panel Queues).

Once this is done, you have to run the following commands to execute the application:

1. Package interface jar:
```
$ cd interface
```
```
$ mvn install
```
2. Move to external-service directory and run external-service:
```
$ cd external-service
```
```
$ mvn spring-boot:run
```
3. Move to worker directory and run worker:
```
$ cd worker
```
```
$ mvn spring-boot:run
```
4. Move to server directory and run server:
```
$ cd server
```
```
$ mvn spring-boot:run
```

Now you can open new browser in http://localhost:8080/ and introduce a text in the box. Then, click in "Create Task" button to create a new task. Once this is done, you can follow the task progress by:
- looking at the browser console.
- looking at the terminal console where the server is running.
- looking at the terminal console where the worker is running.
- looking at the terminal console where the external-service is running.

When a new task is created, its ID is returned to the client, who can visualize the progress of the task in http://localhost:8080/tasks/{id}
If the task has finished or has not started, in this URL will only appear the task ID and the task text. 

### Example

Once the application is already running, if you make the following request:

```
http://localhost:8080/
```

You get the price 38.95 EUR

## Running the tests ✅

To execute the tests, just run the following command:

```
$ mvn test
```
These tests test different situations, including error situations.


## Documentation 📄

You can access the API documentation with the following request:

```
http://localhost:8080/swagger-ui/index.html
```

## Built With ⚒️

* [Spring Boot](https://spring.io) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [JUnit](https://junit.org/junit5/) - Testing
* [OpenJDK](https://openjdk.org) - Java

## Authors 👩🏼‍💻

* **María Duque Román** - [mariaduq](https://github.com/mariaduq)

