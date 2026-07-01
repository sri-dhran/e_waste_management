# Stage 1: Build the Maven application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY backend /app/backend
COPY frontend /app/frontend
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Stage 2: Run the packaged application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/backend/target/ewaste-management-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /app/frontend /app/frontend

# Create uploads and qr directories inside /app
RUN mkdir -p /app/uploads /app/qr

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
