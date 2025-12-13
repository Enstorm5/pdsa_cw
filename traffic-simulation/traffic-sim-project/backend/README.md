# Backend - Traffic Simulation (Spring Boot)

## Setup
- Java 17+, Maven
- MySQL database `trafficsimdb` (create manually or change application.properties)
- Run: `mvn spring-boot:run` from backend directory

## API
- GET /api/game/new -> returns a random capacity matrix and edges list.
- POST /api/game/solve -> body { matrix: [...], reported: number, name: string } returns computed flows and times.

