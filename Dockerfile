FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY backend/target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=container", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]