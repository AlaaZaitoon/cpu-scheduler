# Build stage
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy Maven wrapper and build configuration
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml pom.xml

# Ensure mvnw has execution permissions
RUN chmod +x mvnw

# Copy source code
COPY src src

# Build the application jar file (skipping tests for speed)
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/cpu-scheduler-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
