FROM openjdk:21-slim

WORKDIR /app

COPY target/technihongo.jar technihongo.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "technihongo.jar"]

