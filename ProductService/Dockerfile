FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar product-service.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","product-service.jar"]
