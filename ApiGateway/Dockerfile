FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar api-gateway.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","api-gateway.jar"]
