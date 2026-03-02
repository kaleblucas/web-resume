# Web Resume

A professional web-based resume built with Javalin (Java backend) and modern frontend technologies.

## Features

- Clean, responsive design
- JSON and XML formatters
- Professional styling with custom themes
- Lightweight Javalin backend serving static assets
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
- Java 11+
- Maven 3.6+
- Docker (optional)

### Run locally:
```bash
mvn clean package
java -jar target/web-resume-1.0-SNAPSHOT.jar
```

Then open http://localhost:8080

### Run with Docker:
```bash
docker-compose up -d --build
```

## Project Structure

```
.
├── src/
│   └── main/
│       ├── java/dev/kaleblucas/resume/
│       │   └── Main.java          # Javalin server entry point
│       └── resources/static/
│           ├── index.html         # Main resume page
│           ├── styles.css         # Primary styling
│           ├── script.js          # Interactive functionality
│           ├── *-formatter.html   # JSON/XML formatters
│           └── ...
├── docker-compose.yml             # Container configuration
├── Dockerfile                      # Docker build instructions
├── pom.xml                        # Maven dependencies
└── README.md
```

## Technology Stack

- **Backend:** Javalin (lightweight Java web framework)
- **Frontend:** HTML5, CSS3, JavaScript
- **Build:** Maven
- **Deployment:** Docker

## License

MIT License - see LICENSE file for details
