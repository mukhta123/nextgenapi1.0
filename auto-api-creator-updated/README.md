# Auto API Creator - Updated (dynamic endpoints + React UI)

This project includes:
- Backend: Spring Boot app that can generate model/service/controller files and register a dynamic POST endpoint at runtime.
- Frontend: Simple React app that posts model definitions and calls the generated dynamic endpoint.

Run backend:
1. Java 17 + Maven installed.
2. From project root:
   mvn spring-boot:run

API:
POST /api/generate -> generates files and registers endpoint
POST /api/{modelName} -> dynamic endpoint (registered after generation)

Front-end:
1. cd frontend
2. npm install
3. npm start
