# Build stage: use Maven with Temurin 21 (compiles with JDK 21)
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only what's needed first to leverage Docker cache
COPY pom.xml .
COPY src ./src

# Build (batch mode, skip tests to speed up)
RUN mvn -B -DskipTests clean package

# Run stage: use Java 21 runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage (wildcard handles versioned names)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
