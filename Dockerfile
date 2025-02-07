FROM maven:3.8.6-openjdk-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/file-deduplication-app-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
