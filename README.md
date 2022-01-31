---
title: Configure a Sample Application 
description: LinkedIn API documentation for sample application configuration
author: Deeksha-ramesh
ms.date: 01/31/2022
ms.topic: article
ms.author: li_dramesh
ms.service: linkedin
---

# Configure a Sample Application for LinkedIn OAuth APIs

## Features

A sample application can be used by the developers to download and try out LinkedIn's APIs. This application demonstrates the best practices in implementing LinkedIn's Externalized OAuth APIs.

The application contains client and server components to manage API calls with LinkedIn's API endpoints. The server creates an access token, stores the access token, and invokes API calls with the payload upon request from the client.
The sample application uses the following technologies:

* Spring Boot: Used as web server framework [<https://spring.io/projects/spring-boot>]
* LinkedIn OAuth 2.0: Authenticate with LinkedIn APIs
* Maven
* Java 7+

## Prerequisites

* You need to have an application registered in [LinkedIn Developer Portal](https://developer.linkedin.com/)
Once you have your application, note down the Client ID and Client Secret
* Add <http://localhost:8080/login> to the Authorized Redirect URLs under the **Authentication** section
* Configure the application build by installing MAVEN using [Installing Apache Maven](https://maven.apache.org/install.html)

## Configure the application

**Configure the client app:**

 1. Navigate to the **application.properties** file. You can find this file under: **/client/src/main/resources/application.properties**
 1. To edit server link or port with custom values modify the following values:

    ```json
    server.port = <replace_with_required_port_no>
    SERVER_URL = <replace_with_required_server_url>
    ```

 1. Save the changes.

**Configure the server app:**

 1. Navigate to the **config.properties** file. You can find this file under: **/server/src/main/resources/config.properties**
 1. Edit the following properties in the file with your client credentials:

    ```json
    clientId = <replace_with_client_id>
    clientSecret = <replace_with_client_secret>
    redirectUri = <replace_with_redirect_url_set_in_developer_portal>
    scope = <replace_with_api_scope>
    client_url = <replace_with_client_url>
    ```

 1. Save the changes.
  
## Start the application

To start the server:

1. Navigate to the server folder
2. Open the terminal and run the following command to install dependencies:
`mvn install`
3. Execute the following command to run the spring-boot server:
`mvn spring-boot:run`

> [!NOTE]
> The server will be running on <http://localhost:8080/>

To start the client:

1. Navigate to the client folder
2. Open the terminal and run the following command to install dependencies:
 `mvn install`
3. Execute the following command to run the spring-boot server:
`mvn spring-boot:run`

> [!NOTE]
> The client will be running on <http://localhost:8989/>

## List of dependencies

|Component Name |License |Linked |Modified |
|---------------|--------|--------|----------|
|[boot:spring-boot-starter-parent:2.5.2](<https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-parent/2.5.2>) |Apache 2.0 |Static |No |
|[boot:spring-boot-starter-parent:2.5.2](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-parent/2.5.2) |Apache 2.0 |Static |No |
|[org.springframework.boot:spring-boot-starter-thymeleaf:2.2.2.RELEASE](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf/2.2.2.RELEASE) |Apache 2.0 |Static |No |
|[org.springframework.boot:spring-boot-devtools:2.6.0](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools/2.6.0) |Apache 2.0 |Static |No |
|[com.fasterxml.jackson.core:jackson-databind:2.13.0](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind/2.13.0)                                     |Apache 2.0 |Static |No |
|[com.fasterxml.jackson.core:jackson-core:2.13.0](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core/2.13.0) |Apache 2.0 |Static |No |
|[org.springframework.boot:spring-boot-starter-web:2.5.2](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/2.5.2) |Apache 2.0 |Static |No |
| [org.springframework.boot:spring-boot-starter-test:2.6.0](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test/2.6.0) |Apache 2.0 |Static |No |
|[org.springframework:spring-core:5.3.13](https://mvnrepository.com/artifact/org.springframework/spring-core/5.3.13) |Apache 2.0 |Static |No |
