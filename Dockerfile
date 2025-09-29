FROM openjdk:8-jdk-slim
WORKDIR /app
COPY "./target/AdminProManager-0.1.jar" "alianza-web.jar"
EXPOSE 8081
ENTRYPOINT ["java","-jar","alianza-web.jar"]