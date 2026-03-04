# Web Resume

A professional web-based resume built with Javalin (Java backend) and modern frontend technologies.

## Features

- Clean, responsive design with dark mode
- REST API serving resume content dynamically (skills, work experience)
- JSON and XML formatter tools
- Lightweight Javalin backend with Jackson serialization
- Docker containerized for easy deployment

## Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kaleblucas/web-resume.git
   cd web-resume
   ```

2. **Run with Docker:**
   ```bash
   docker-compose up -d
   ```

3. **Access the site:**
   - Open http://localhost:8080

## Development

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker (optional)

### Run locally:
```bash
mvn clean package
java -jar target/app.jar
```

Then open http://localhost:8080

### Run with Docker:
```bash
docker-compose up -d --build
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/skills` | Skill categories with lists |
| GET | `/api/experience` | Work experience with bullets and tech tags |

## Project Structure

```
.
├── src/
│   └── main/
│       ├── java/dev/kaleblucas/resume/
│       │   ├── Main.java                  # Javalin server entry point
│       │   ├── api/ResumeRoutes.java      # REST route registration
│       │   ├── data/ResumeData.java       # Static data provider
│       │   └── model/
│       │       ├── SkillCategory.java     # Record: name, skills
│       │       └── Experience.java        # Record: company, role, tenure, ...
│       └── resources/static/
│           ├── index.html                 # Main resume page
│           ├── styles.css                 # Primary styling
│           ├── script.js                  # Nav, scroll, dark mode, fade observer
│           ├── resume.js                  # Fetches API and renders skills/work
│           ├── clipboard-utils.js         # Shared clipboard helper
│           ├── *-formatter.html/js        # JSON/XML formatters
│           └── ...
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## Technology Stack

- **Backend:** Javalin 6 (lightweight Java web framework)
- **Serialization:** Jackson 2.17
- **Frontend:** HTML5, CSS3, vanilla JavaScript
- **Build:** Maven (fat JAR via maven-shade-plugin)
- **Deployment:** Docker
