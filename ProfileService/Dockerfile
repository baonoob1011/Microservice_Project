FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar profile-service.jar
EXPOSE 8089
ENTRYPOINT ["java","-jar","profile-service.jar"]
