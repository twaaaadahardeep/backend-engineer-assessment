# Stage 1: Build stage
FROM gradle:jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build --no-daemon

# Stage 2: Staging environment
FROM openjdk:21-jdk-slim AS staging
WORKDIR /app
COPY --from=build /app/build/libs/app-*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 3: Production environment
FROM openjdk:21-jdk-slim AS production
WORKDIR /app
COPY --from=build /app/build/libs/app-*.jar ./app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]