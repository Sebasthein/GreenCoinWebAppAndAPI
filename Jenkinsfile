pipeline {
    agent any
    
    tools {
        maven 'Maven-3' 
        jdk 'Java-21'
        // Docker ya está instalado en el sistema, no necesitamos tool automática
    }

    environment {
        // --- CONFIGURACIÓN DB ---
        // 'pg-jenkins' es el nombre del contenedor de Postgres
        SPRING_DATASOURCE_URL = "jdbc:postgresql://pg-jenkins:5432/reciclaje"
        SPRING_DATASOURCE_USERNAME = "postgres"
        SPRING_DATASOURCE_PASSWORD = "secret" 
        SPRING_JPA_HIBERNATE_DDL_AUTO = "update"
        
        // --- CONFIGURACIÓN DOCKER ---
        IMAGE_NAME = "mi-app-springboot"
        CONTAINER_NAME = "mi-app-container"
        NETWORK_NAME = "red-jenkins"  // <--- ¡Confirma que tu red se llama así!
        PORT_HOST = "9090"
    }

    stages {
        stage('Verificar Entorno 🕵️‍♂️') {
            steps {
                sh 'java -version'
                sh 'docker version' // Debería mostrar Cliente y Servidor
            }
        }
        
        stage('Construcción del JAR 🛠️') {
            steps {
                echo 'Construyendo el artefacto .jar con Maven...'
                sh 'mvn clean package -DskipTests' 
            }
        }

        stage('Construcción de Imagen Docker 🐳') {
            steps {
                script {
                    echo 'Construyendo la imagen Docker...'
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Despliegue de Contenedor 🚀') {
            steps {
                script {
                    echo 'Deteniendo contenedor anterior (si existe)...'
                    sh "docker rm -f ${CONTAINER_NAME} || true"

                    echo 'Desplegando nuevo contenedor...'
                    sh """
                        docker run -d \
                        --name ${CONTAINER_NAME} \
                        --network ${NETWORK_NAME} \
                        -p ${PORT_HOST}:8080 \
                        -e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
                        -e SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} \
                        -e SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD} \
                        ${IMAGE_NAME}:latest
                    """
                }
            }
        }
    }
}
