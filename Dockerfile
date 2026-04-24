# Step 1: Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run Stage
# Changed from openjdk:17-jdk-slim to eclipse-temurin
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Using wildcard to ensure it finds the JAR regardless of naming
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
