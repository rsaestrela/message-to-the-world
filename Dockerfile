FROM openjdk:8u212-jdk-slim

ARG VERSION
ARG PORT

VOLUME /tmp

EXPOSE ${PORT}

ADD target/mttw-${VERSION}.jar mttw.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mttw.jar"]