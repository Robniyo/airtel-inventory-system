# --- STAGE 1: Build ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Cache dependencies first (makes future builds much faster)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Run ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# The wildcard *.jar handles cases where Maven adds version numbers
# Renaming it to app.jar here makes the ENTRYPOINT consistent
COPY --from=build /app/target/*.jar app.jar

# Standard Spring Boot port
EXPOSE 8080

# -Xmx512m prevents Render from killing the app for high RAM usage
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
