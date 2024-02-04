#
# Package stage
#
FROM openjdk:17.0.2-jdk
EXPOSE 8083
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]