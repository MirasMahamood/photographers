FROM --platform=linux/amd64 openjdk:17-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY build/libs/photographers-0.0.1.jar photographers.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /photographers.jar"]
