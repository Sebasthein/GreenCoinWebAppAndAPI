# Opción 1: OpenJDK 21 alternativo
FROM openjdk:21-slim

# Opción 2: Si no funciona, usa esta
# FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiar Maven wrapper
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8080

# Ejecutar
CMD ["java", "-jar", "target/reciclaje-0.0.1-SNAPSHOT.jar"]
