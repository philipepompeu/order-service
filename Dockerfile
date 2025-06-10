# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Cria diretório de trabalho
WORKDIR /app

# Copia todos os arquivos do projeto para dentro da imagem
COPY . .

# Executa o build do projeto (pulando os testes)
RUN mvn clean package -DskipTests

# Etapa 2: imagem final mais enxuta
FROM openjdk:17-jdk-slim

# Define diretório de trabalho
WORKDIR /app

# Copia o JAR da imagem anterior
COPY --from=builder /app/target/*.jar app.jar

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=45s --timeout=9s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

