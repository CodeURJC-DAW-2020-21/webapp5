FROM maven as builder
COPY ../victorious_web /code/
WORKDIR /code
RUN mvn package -DskipTests

FROM openjdk:11
RUN mkdir -p /usr/src/app
COPY --from=builder /code/target/*.jar /usr/src/app
WORKDIR /usr/src/app
CMD ["java", "-jar", "victorious_web-0.0.1-SNAPSHOT.jar"]