# NEU-CS6650 - Assignment 3
This assignment's implementation focuses on making the swipe data persistent. Requests sent from the Client to POST server will then be queued in RabbitMQ, consumed by the consumer and put to DynamoDB table. Any call to GET server will have it retrieve the data from DynamoDB using DAO class.

## 1. Client UML
![cient-uml](./ClientUML.png)

## 2. POST Server UML
![post-server-uml](./POST-Server-UML.png)

## 3. Consumer UML
![queue-consumer-uml](./Consumer-UML.png)

## 4. GET Server UML
![get-server-uml](./GET-Server-UML.png)