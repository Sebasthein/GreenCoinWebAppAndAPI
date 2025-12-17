#!/bin/bash
# Script de inicio para Render
echo "Iniciando aplicación Spring Boot..."

# Verificar variables de entorno
if [ -z "$DATABASE_URL" ]; then
    echo "ERROR: DATABASE_URL no está configurada"
    exit 1
fi

if [ -z "$JWT_SECRET" ]; then
    echo "ERROR: JWT_SECRET no está configurada"
    exit 1
fi

# Configurar perfil de producción
export SPRING_PROFILES_ACTIVE=prod

# Iniciar la aplicación
echo "Ejecutando: java -jar target/reciclaje-0.0.1-SNAPSHOT.jar"
exec java -jar target/reciclaje-0.0.1-SNAPSHOT.jar
