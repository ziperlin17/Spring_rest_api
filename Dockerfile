# Use a base image with Java installed
# FROM adoptopenjdk:11-jdk-hotspot
# Use an official Java runtime as the base image
FROM openjdk:17-jdk

# Set the working directory inside the container 
WORKDIR /eternal_rest

# Expose the port on which the Spring Boot application will listen
EXPOSE 8080

VOLUME /tmp

RUN mkdir -p /app/
RUN mkdir -p /app/logs/

ADD build/libs/eternal_rest-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar"]