FROM amazoncorretto:17

VOLUME /tmp

ARG JAR_FILE=./build/libs/server-0.0.1.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
