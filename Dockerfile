FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine

WORKDIR /ecosystem-business-service

COPY /target/ecosystem-business-service.jar .

ENTRYPOINT ["java", "-jar", "./ecosystem-business-service.jar"]
