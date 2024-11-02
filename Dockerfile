# Use JDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file (update the path if needed)
COPY target/UserService-0.0.1-SNAPSHOT.jar /app/UserService.jar

# Define the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/UserService.jar"]