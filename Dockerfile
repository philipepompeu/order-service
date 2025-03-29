# Usa uma imagem base com JDK 17
FROM openjdk:17-jdk-slim as builder

# Copia o arquivo JAR gerado pelo Maven
COPY /target/app.jar app.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]