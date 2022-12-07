FROM openjdk:8-jdk-alpine
ADD target/emailservice-0.0.1-SNAPSHOT.jar emailservice.jar
ENTRYPOINT ["java","-jar","emailservice.jar"]