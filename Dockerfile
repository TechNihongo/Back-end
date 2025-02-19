FROM openjdk:21
EXPOSE 3000
ADD target/technihongo.jar technihongo.jar
ENTRYPOINT ["java", "-jar","/technihongo.jar"]