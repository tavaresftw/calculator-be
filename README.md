# Calculator

## Introduction

This project is a web calculator that supports basic operations such as addition, subtraction, multiplication, and division, as well as generating random strings.

## Prerequisites

- Java 11
- Gradle 7.3.3
- Docker

## Installation

1. Clone the repository: `git clone https://github.com/username/calculator.git`
2. Navigate to the project directory: `cd calculator`
3. Build the project: `gradle build`
4. Start the services with Docker: `docker-compose up`

## Usage

To perform an operation, send a POST request to `http://localhost:8080/operation` with a JSON request body that includes the basic math operations and a random string generator. For example:
    
    ```json
    {
        "operationType": "ADDITION",
        "num1": "1.00",
        "num2": "2.00"
    }
    ```