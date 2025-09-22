FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8881
ENTRYPOINT ["java","-jar","config-server.jar"]
