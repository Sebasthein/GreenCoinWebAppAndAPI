FROM eclipse-temurin:21-jdk

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

EXPOSE 8080

CMD ["java", "-jar", "target/reciclaje-0.0.1-SNAPSHOT.jar"]
