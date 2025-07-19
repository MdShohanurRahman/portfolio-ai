# Step 1: Build Stage
FROM eclipse-temurin:24-jdk-alpine AS build
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy Maven project files
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and run the Maven install
COPY src ./src
RUN mvn clean install -DskipTests

# Step 2: Runtime Stage
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]