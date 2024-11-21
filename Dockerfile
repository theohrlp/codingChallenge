# Use an OpenJDK image
FROM openjdk:17-jdk-slim

# Copy the built JAR file into the container
ADD target/challenge-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]