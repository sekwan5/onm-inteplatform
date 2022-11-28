FROM openjdk:11-jre-slim
ADD ./target/onm-ba-0.0.1-SNAPSHOT.jar onm-ba-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-jar", "/onm-ba-0.0.1-SNAPSHOT.jar"]