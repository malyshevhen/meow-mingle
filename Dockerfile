FROM maven:3.8-amazoncorretto-17 AS builder
WORKDIR /app
ADD . .
RUN ./gradlew openApiGenerate

WORKDIR /app/build/generated/sources/openapi
RUN mvn clean install

WORKDIR /app
RUN ./gradlew bootJar

FROM amazoncorretto:17.0.8-alpine AS runner

WORKDIR app/
COPY --from=builder app/build/libs/meow-mingle-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]