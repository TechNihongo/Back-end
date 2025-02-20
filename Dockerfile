FROM openjdk:21-slim
WORKDIR /app
COPY target/technihongo.jar technihongo.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "technihongo.jar"]

FROM mcr.microsoft.com/mssql/server:2019-latest
ENV ACCEPT_EULA=Y
ENV SA_PASSWORD=Max.5123
ENV MSSQL_PID=Express
RUN mkdir -p /var/opt/mssql/data
CMD ["/opt/mssql/bin/sqlservr"]


