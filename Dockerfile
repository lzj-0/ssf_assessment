FROM maven:3.9.9-eclipse-temurin-23 AS builder

ARG APP_DIR=/myapp

WORKDIR ${APP_DIR}

COPY src src
COPY .mvn .mvn
COPY pom.xml .
COPY mvnw .

RUN mvn clean package -Dmaven.test.skip=true

FROM maven:3.9.9-eclipse-temurin-23

ARG DIR=/app

WORKDIR ${DIR}

COPY --from=builder /myapp/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

HEALTHCHECK --interval=60s --timeout=10s --start-period=120s --retries=3 \
    CMD curl -s -f http://localhost:${SERVER_PORT}/status || exit 1

ENTRYPOINT java -jar app.jar