# ────────────────────────────────────────────────────────────────────────────────
# Stage 1: Build
# ────────────────────────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy dependency manifest first so Docker layer-caches the dependency download
# separately from source changes.
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build the fat JAR
COPY src ./src
RUN mvn package -q -DskipTests

# ────────────────────────────────────────────────────────────────────────────────
# Stage 2: Runtime
# ────────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Non-root user for better container security
RUN addgroup -S resume && adduser -S resume -G resume
USER resume

COPY --from=builder /build/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
