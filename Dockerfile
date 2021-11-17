# For Java 8, try this
# FROM openjdk:8-jdk-alpine

# For Java 11, try this
FROM adoptopenjdk/openjdk13:ubi


#create the application directory
RUN mkdir /opt/app

# cd /opt/app
WORKDIR /opt/app


# cp target/spring-boot-web.jar /opt/app/aidingafrica.jar
COPY ./target/quizapi-0.0.1-SNAPSHOT.jar ./quizapi-0.0.1-SNAPSHOT.jar


# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","quizapi-0.0.1-SNAPSHOT.jar"]
RUN ls .