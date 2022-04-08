![Carey Development Logo](http://careydevelopment.us/img/branding/careydevelopment-logo-sm.png)

# Carey Development Ecosystem User Service
![](https://img.shields.io/badge/jdk-11-blue.svg) ![license](https://img.shields.io/badge/license-MIT-blue.svg)
![](https://img.shields.io/badge/maven-3.6.3-blue.svg)

This is a Spring Boot application that handle business-related transactions.

Among the transactions available:
- Persisting business info
- Retrieving business info by ID
- Updating business info
- Retrieving all stored industry names with corresponding NAICS codes

## Persisting Business Info
POST a request to the `/businesses` endpoint with a valid JSON request body to save business info.

## Retrieving Business Info
Use a GET request at the endpoint `/businesses/{Id}`. The response is in JSON format.

## Updating Business Info
PUT a request at the `/businesses` endpoint with a valid JSON request body to update business info.

## Retrieve NAICS Info
Use a GET request at the `/industries` endpoint to retrieve a JSON-formatted array of industries
with their corresponding NAICS ID's.

## License
This code is under the [MIT License](https://github.com/careydevelopment/ecosystem-business-service/blob/master/LICENSE).