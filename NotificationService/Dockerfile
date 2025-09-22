FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar notification-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","notification-service.jar"]
