# Traffic Simulation Problem - Full Project Skeleton

This repository contains a skeleton full-stack project for the Traffic Simulation Game:
- Backend: Spring Boot (Java, Maven) - implements Edmonds-Karp & Dinic algorithms, REST API, and saving player results to MySQL.
- Frontend: React + Vite - simple UI displaying graph, submitting guess, and showing results.

This skeleton includes:
- Algorithm implementations (correct and tested by unit tests)
- API endpoints
- Example unit test (MaxFlowServiceTest)
- Basic front-end that calls the API

Notes:
- You need to create the MySQL database and update `backend/src/main/resources/application.properties`.
- The backend uses Java 17 and Spring Boot. Build with Maven.
- This is a skeleton to be extended: add better validation, UI polish, authentication if needed.

