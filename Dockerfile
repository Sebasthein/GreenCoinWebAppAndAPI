# Usamos una imagen base ligera de Java 21 (igual que en tu pipeline)
FROM eclipse-temurin:21-jdk-alpine

# Creamos un volumen para archivos temporales (útil para Spring Boot)
VOLUME /tmp

# Copiamos el jar que generó Jenkins (el asterisco es por si cambia la versión)
COPY target/*.jar app.jar

# El comando que se ejecutará al iniciar el contenedor
ENTRYPOINT ["java","-jar","/app.jar"]
