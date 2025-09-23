FROM openjdk:21-jdk
WORKDIR /app
EXPOSE 8080
COPY target/honey_store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
