FROM gradle:6.0.1-jdk11 as build

WORKDIR /usr/src/app

COPY settings.gradle build.gradle ./
COPY src/ src/

RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

COPY --from=build /usr/src/app/build/libs/emergencies-*.jar /usr/app/emergencies.jar

EXPOSE 12345
ENTRYPOINT ["java", "-jar", "/usr/app/emergencies.jar"]
