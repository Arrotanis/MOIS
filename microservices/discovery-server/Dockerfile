# Use a base image with OpenJDK 17
FROM openjdk:17-jdk

# Copy the compiled JAR file from the target directory into the container
COPY target/*.jar app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
