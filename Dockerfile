FROM maven:4.0.0-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/dlc-0.0.1-SNAPSHOT.jar dlc.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","dlc.jar"]