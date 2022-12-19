FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} emailservice.jar
ENTRYPOINT ["java","-jar","emailservice.jar"]