#!/bin/bash

# Detener y eliminar el contenedor si ya se encuentra en ejecución
echo "Deteniendo el contenedor anterior si existe..."
docker rm -f greencoin-app-container 2>/dev/null

# Limpiar y empaquetar la aplicación de Spring Boot (omitiendo los tests para mayor rapidez)
echo "Empaquetando la aplicación..."
./mvnw clean package -DskipTests

# Construir la imagen Docker usando el Dockerfile local
echo "Construyendo la imagen Docker..."
docker build -t greencoin-app .

# Correr el contenedor mapeando el puerto 8080 y conectándose a la base de datos local
echo "Iniciando el contenedor..."
docker run -d --rm \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/reciclaje_db \
  -e SPRING_PROFILES_ACTIVE=default \
  --name greencoin-app-container \
  greencoin-app

echo "================================================="
echo "¡La aplicación ha sido iniciada en el puerto 8080!"
echo "================================================="
echo "Para ver los logs en tiempo real de la app, ejecuta:"
echo "docker logs -f greencoin-app-container"
