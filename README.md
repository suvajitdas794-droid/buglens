# BugLens 🐞

BugLens is a dynamic Java + Spring Boot developer tool that analyzes stack traces and logs, classifies common failures, explains likely root causes, and produces practical debugging steps.

## Features

- Dynamic dashboard with live statistics
- Error/log analyzer
- Java, Spring Boot, MySQL, JSP/Tomcat, security, network and JVM pattern detection
- Confidence percentage
- Severity classification
- Root-cause explanation
- Recommended debugging plan
- Saved investigation history
- Delete investigations
- Sample error library
- Playground/pattern scanner
- Responsive dark developer UI
- MySQL persistence through Spring Data JPA
- REST API endpoints

## Tech Stack

Java 17, Spring Boot 3.5.6, Spring Web, Spring Data JPA, MySQL, Thymeleaf, HTML5, CSS3, JavaScript.

## Setup

1. Create/open the project in Eclipse as an existing Maven project.
2. Make sure MySQL is running.
3. Update `src/main/resources/application.properties` with your MySQL username/password.
4. The application creates `buglens_db` automatically when the configured MySQL user has permission.
5. Run `BugLensApplication.java`.
6. Open `http://localhost:8080`.

## REST endpoints

- `POST /api/analyze`
- `GET /api/analyses`
- `GET /api/analyses/{id}`
- `DELETE /api/analyses/{id}`
- `GET /api/stats`

Example request:

```json
{
  "title": "MySQL login failure",
  "log": "java.sql.SQLException: Access denied for user 'root'@'localhost'"
}
```

## GitHub description

> BugLens is a full-stack Spring Boot developer tool that analyzes stack traces and logs, detects common application failures, explains likely root causes, and generates actionable debugging steps.

## Future upgrades

- Spring Security + JWT accounts
- Real AI provider integration
- PDF/ZIP log upload parsing
- GitHub issue creation
- Docker support
- JUnit integration tests
- Team workspaces
- Shareable investigation links
- Error trend analytics
