# PaymentGateway
The Payment Gateway is a practical exercise designed to evaluate your ability to build a simple, functional payment system using modern Java technologies. The application serves as a RESTful API that supports two fictional payment methods: A Pay and B Pay. 
# Payment Gateway Take-Home Assignment

## Overview
A simple RESTful Payment Gateway API supporting A Pay and B Pay methods.

## How to Build and Run
1. Clone the repo: `git clone <repo-url>`
2. Build: `mvn clean install`
3. Run: `mvn spring-boot:run` or `java -jar target/payment-gateway-0.0.1-SNAPSHOT.jar`
4. Access API at http://localhost:9391
POST http://localhost:9391/payments
POST http://localhost:9391/payments/1/approve
GET http://localhost:9391/payments/1
GET http://localhost:9391/payments?page=0&size=5
## How to Test
- Use Postman or curl.
- Create Payment: POST /payments with JSON {"method":"A Pay", "amount":100.0, "reference":"ref123"}
- Approve: POST /payments/1/approve with JSON {"code":"123456"}
- Get by ID: GET /payments/1
- List: GET /payments?page=0&size=10

## Assumptions/Limitations
- Hardcoded PIN/OTP for simulation.
- H2 in-memory DB resets on restart.
- Basic delay simulation for B Pay.
- No production security; use API key header "X-API-KEY: api-key-secret" if implemented.

##Database (application properties)
I used Mysql database name as Paymentdbs
username:root
password:Loveudad@143
