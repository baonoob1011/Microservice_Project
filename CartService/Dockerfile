FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar cart-service.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","cart-service.jar"]
