FROM node as node
COPY ./frontend /front/
WORKDIR /front
RUN npm ci
RUN npm run build --prod --base-href="/new/"

FROM maven as builder
COPY ./victorious_web/ /code/
WORKDIR /code
RUN mkdir -p /code/victorious_web/src/main/resources/static/new
COPY --from=node /front/dist/frontend/* /code/victorious_web/src/main/resources/static/new
RUN mvn package -DskipTests

FROM openjdk:11
RUN mkdir -p /usr/src/app
COPY --from=builder /code/target/*.jar /usr/src/app
WORKDIR /usr/src/app
CMD ["java", "-jar", "victorious_web-0.0.1-SNAPSHOT.jar"]