pipeline {
    agent any
    
    tools {
        maven 'Maven-3' 
        jdk 'Java-21' 
    }

    environment {
        // --- CONFIGURACIÓN DB ---
        // Nota: Para el "build" de Maven, seguimos usando el nombre del host 'pg-jenkins'
        SPRING_DATASOURCE_URL = "jdbc:postgresql://pg-jenkins:5432/reciclaje"
        SPRING_DATASOURCE_USERNAME = "postgres"
        SPRING_DATASOURCE_PASSWORD = "secret" 
        SPRING_JPA_HIBERNATE_DDL_AUTO = "update"
        
        // --- CONFIGURACIÓN DOCKER ---
        IMAGE_NAME = "mi-app-springboot"
        CONTAINER_NAME = "mi-app-container"
        // ¡IMPORTANTE! Pon aquí el nombre real de tu red docker
        NETWORK_NAME = "red-jenkins" 
        PORT_HOST = "9090"
    }

    stages {
        stage('Verificar Entorno 🕵️‍♂️') {
            steps {
                sh 'java -version'
                sh 'docker --version' // Verificamos que Jenkins vea a Docker
            }
        }
        
        stage('Construcción del JAR 🛠️') {
            steps {
                echo 'Construyendo el artefacto .jar con Maven...'
                sh 'mvn clean package -DskipTests' 
                // Nota: A veces saltamos tests aquí si vamos a testear la imagen, 
                // pero si quieres correrlos, quita -DskipTests.
            }
        }

        stage('Construcción de Imagen Docker 🐳') {
            steps {
                script {
                    echo 'Construyendo la imagen Docker...'
                    // Esto usa el Dockerfile que acabamos de crear
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Despliegue de Contenedor 🚀') {
            steps {
                script {
                    echo 'Deteniendo contenedor anterior (si existe)...'
                    // El || true evita que el pipeline falle si el contenedor no existe aún
                    sh "docker rm -f ${CONTAINER_NAME} || true"

                    echo 'Desplegando nuevo contenedor...'
                    // --network: Vital para que vea a Postgres
                    // -p: Mapeamos el puerto 9090 de tu PC al 8080 del contenedor
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
    
    post {
        success {
            echo '¡Éxito total! La aplicación está corriendo en Docker en el puerto 9090.'
        }
        failure {
            echo '😱 Algo falló. Revisa los logs.'
        }
    }
}
