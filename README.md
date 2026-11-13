# Microservices Demo (Spring Boot 3 + Java 21)

| Service | Port | Endpoints |
|---|---|---|
| user-service | 8081 | GET /users, GET /users/{id}, POST /users |
| booking-service | 8082 | GET /bookings, GET /bookings/{id}, POST /bookings |

booking-service -> user-service REST call karala user validate karanawa.
Health check (ALB walata): GET /actuator/health

## Run (terminal 2k)
    cd user-service    && mvn spring-boot:run
    cd booking-service && mvn spring-boot:run

## Test
    curl localhost:8081/users
    curl -X POST localhost:8082/bookings -H "Content-Type: application/json" \
         -d '{"userId":1,"route":"Matara-Colombo","seats":2}'
    # userId=99 dunnoth 400 ewi

Build jar: mvn clean package  ->  target/*.jar
