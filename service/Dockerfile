FROM gradle:6.0.1-jdk11 as build

WORKDIR /usr/src/app

COPY settings.gradle build.gradle ./
COPY src/ src/

RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

COPY --from=build /usr/src/app/build/libs/emergencies-*.jar /usr/app/emergencies.jar

ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 12345
ENTRYPOINT ["/bin/bash", "-c", "java -jar /usr/app/emergencies.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
