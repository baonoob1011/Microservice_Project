FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/cart-service.jar ./cart-service.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","cart-service.jar"]
