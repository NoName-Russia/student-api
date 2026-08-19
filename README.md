# Student API

Spring Boot 4 + MySQL + HTML/CSS/JS application for student registration with 2GIS university autocomplete.

## Structure

- `src/main/java/com/electrostore/studentapi/entity/Student.java` — student entity.
- `src/main/java/com/electrostore/studentapi/repository/StudentRepository.java` — JPA repository.
- `src/main/java/com/electrostore/studentapi/controller/UniversityController.java` — university search endpoint.
- `src/main/java/com/electrostore/studentapi/service/UniversityService.java` — 2GIS API integration.
- `src/main/resources/static/` — frontend.

## Run locally

1. Create MySQL database:

```sql
CREATE DATABASE student_api;
```

2. Copy `.env.example` to `.env`.
3. Put your 2GIS API key into `.env`:

```text
2GIS_API_KEY=YOUR_REAL_KEY
```

4. Check MySQL credentials in `src/main/resources/application.properties`.
5. Run `StudentApiApplication` from IntelliJ IDEA.
6. Open `http://localhost:8080/`.

## API

University autocomplete:

`GET /api/universities/search?query=мгу`

Student registration:

`POST /api/students`

Example body:

```json
{
  "firstName": "Максим",
  "lastName": "Пурас",
  "email": "max@example.com",
  "university": "МГУ",
  "universityId": "123"
}
```

The 2GIS key is intentionally loaded from local configuration and must not be committed to GitHub.
