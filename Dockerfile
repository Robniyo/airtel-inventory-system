# Step 1: Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run Stage (Fixed for 2026 compatibility)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# This copies the app.jar we created in the pom.xml finalName
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
