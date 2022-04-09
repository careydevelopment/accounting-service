FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine

WORKDIR /accounting-service

COPY /target/accounting-service.jar .

ENTRYPOINT ["java", "-jar", "./accounting-service.jar"]
