FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY platform-service/pom.xml platform-service/pom.xml
COPY api-gateway/src api-gateway/src

RUN mvn -pl api-gateway -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /workspace/api-gateway/target/api-gateway-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
