FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY platform-service/pom.xml platform-service/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY platform-service/src platform-service/src

RUN mvn -pl platform-service -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /workspace/platform-service/target/platform-service-*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
