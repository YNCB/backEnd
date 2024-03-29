FROM openjdk:11-jre

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod","app.jar"]