FROM openjdk:8-jdk-slim
WORKDIR /app
COPY "./target/AdminProManager-0.1.jar" "apm.jar"
EXPOSE 8088
ENTRYPOINT ["java","-jar","apm.jar"]