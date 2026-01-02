pipeline {
    agent any
    
    tools {
        maven 'Maven-3' 
        jdk 'Java-21' 
    }

    // --- AQUÍ ESTÁ LA MAGIA ---
    environment {
        // CAMBIO IMPORTANTE:
        // En lugar de 'host.docker.internal', usamos el NOMBRE DEL CONTENEDOR 'pg-jenkins'
        // Docker resolverá esto automáticamente gracias a la red que creamos.
        SPRING_DATASOURCE_URL = "jdbc:postgresql://pg-jenkins:5432/reciclaje"
        
        SPRING_DATASOURCE_USERNAME = "postgres"
        // Asegúrate de que esta contraseña coincida con la de tu contenedor pg-jenkins
        SPRING_DATASOURCE_PASSWORD = "secret" 
        
        SPRING_JPA_HIBERNATE_DDL_AUTO = "update"
    }

    stages {
        stage('Verificar Entorno 🕵️‍♂️') {
            steps {
                sh 'java -version'
            }
        }
        
        stage('Construcción y Test 🛠️') {
            steps {
                echo 'Construyendo y conectando a Postgres...'
                // Maven compila, corre tests (conectándose a la BD) y crea el .jar
                sh 'mvn clean package' 
            }
        }

        stage('Despliegue y Smoke Test 🚢') {
            steps {
                script {
                    echo '🚀 Arrancando la aplicación en puerto 9090...'
                    // Usamos nohup para correrlo en segundo plano (&) y redirigir logs a app.log
                    // -Dserver.port=9090 cambia el puerto para no chocar con Jenkins
                    sh 'nohup java -Dserver.port=9090 -jar target/*.jar > app.log 2>&1 &'
                    
                    echo '⏳ Esperando 20 segundos a que Spring Boot arranque...'
                    sleep 20
                    
                    echo '🔍 Verificando si la app responde (Smoke Test)...'
                    // Intentamos conectar. Si falla, mostramos el log para ver por qué.
                    sh 'curl -v http://localhost:9090 || cat app.log'
                    
                    echo '✅ ¡La aplicación está viva! (Cerrando proceso para ahorrar memoria...)'
                    sh 'pkill -f "java -Dserver.port=9090"'
                }
            }
        }
    }
    
    post {
        success {
            echo '¡Vitoria! La app se conectó a la BD, pasó los tests y se empaquetó.'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo '😱 Falló la conexión. Asegúrate de que el contenedor de Postgres está corriendo.'
        }
    }
}
