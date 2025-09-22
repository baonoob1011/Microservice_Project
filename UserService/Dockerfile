# Stage 1: Build jar with Maven Wrapper
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Make mvnw executable
RUN chmod +x mvnw

# Build jar (skip tests to speed up)
RUN ./mvnw clean package -DskipTests

# Stage 2: Final image
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy jar từ stage builder
COPY --from=builder /app/target/*.jar ./user-service.jar

# Expose port
EXPOSE 8080

# Start app
ENTRYPOINT ["java", "-jar", "user-service.jar"]
