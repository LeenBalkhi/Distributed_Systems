# Distributed Systems

A Java-based implementation of a distributed file storage system inspired by Box.com, utilizing microservices architecture.

## Overview

This project simulates a cloud-based file storage service, allowing users to upload, download, and manage files across distributed nodes. It employs a microservices architecture with components such as Sender, Receiver, Service, and Zuul Gateway to handle various aspects of the system.

## Features

* File upload and download functionality
* Microservices architecture for scalability and maintainability
* Zuul Gateway for routing and load balancing
* Service component for handling business logic
* Sender and Receiver components for managing file transfers

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher
* Maven or Gradle for building the project
* Spring Boot for running the microservices

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/LeenBalkhi/Distributed_Systems.git
   cd Distributed_Systems
   ```

2. Build each microservice using Maven or Gradle:

   ```bash
   mvn clean install
   ```

3. Run each microservice:

   ```bash
   java -jar target/{microservice-name}.jar
   ```

   Replace `{microservice-name}` with the actual name of the microservice JAR file.

## Project Structure

* `Sender/` - Handles file sending operations
* `Receiver/` - Handles file receiving operations
* `Service/` - Contains the core business logic
* `Zuul Gateway/` - Manages routing and load balancing between services
* `README.md` - Project documentation
