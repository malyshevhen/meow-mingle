FROM openjdk:17-alpine
ADD build/libs/meow-mingle-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]