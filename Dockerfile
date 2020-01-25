FROM openjdk:8u212-jdk-slim

ARG SERVER_PORT
ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD
ARG SPRING_ACTIVE_MQ_BROKER-URL
ARG SPRING_ACTIVE_MQ_USER
ARG SPRING_ACTIVE_MQ_PASSWORD

VOLUME /tmp

EXPOSE ${SERVER_PORT}

ADD https://github.com/rsaestrela/message-to-the-world/releases/latest/download/mttw-0.0.1-SNAPSHOT.jar mttw.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mttw.jar"]