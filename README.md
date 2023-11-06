## Introduction

This project is a backend application of built for prophius java developer assessment.

It a mini social media platform.

## Setup

Make sure to follow all these steps exactly as explained below. Do not miss any steps or you won't be able to run this application.

### Start the Server with Docker
Run the following Docker commands in the project root directory to run the application
- `docker build -t social-service . `
-
then run

- `docker run -p 9090:9090 social-service `
  NB: you can bind to any other free port in case 9090 is already in use on your machine

### CREDENTIALS
- DB username is empty
- DB password is empty
- Basic auth username is 'test'
- Basic auth password is '123456'

### Documentation
- Database tables = http://localhost:9090/h2-console/


### DATABASE
- DB = H2 in memory
- migration = Liquibase

### NB
ALL APIS ON THIS SERVICE REQUIRES JWT AUTH GENERATED DURING SIGNUP

Some resource access require basic auth
`username=test`
`password=123456`