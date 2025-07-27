# Stage 1 — Build
FROM gradle:8.7-jdk17 AS build
LABEL authors="ikrom"
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test

# Stage 2 — Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]