# Step 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# This finds your jar even if the name has version numbers
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Adding memory constraints helps avoid crashes on Render Free tier
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
