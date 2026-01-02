pipeline {
    agent any
    
    tools {
        maven 'Maven-3' 
        jdk 'Java-21' 
    }

    // --- AQUÍ ESTÁ LA MAGIA ---
    environment {
        // Sobreescribimos la URL de la base de datos.
        // host.docker.internal apunta a tu ordenador físico desde el contenedor
        SPRING_DATASOURCE_URL = "jdbc:postgresql://host.docker.internal:5432/reciclaje"
        SPRING_DATASOURCE_USERNAME = "postgres"
        SPRING_DATASOURCE_PASSWORD = "secret"
        // Asegura que Hibernate no intente validar cosas raras al inicio
        SPRING_JPA_HIBERNATE_DDL_AUTO = "update"
    }

    stages {
        stage('Verificar Entorno 🕵️‍♂️') {
            steps {
                sh 'java -version'
                // Un comando de depuración para ver si Jenkins ve variables
                sh 'printenv | grep SPRING || true' 
            }
        }
        
        stage('Despliegue y Test de Integración 🚀') {
            steps {
                echo 'Iniciando aplicación contra base de datos real...'
                // ¡YA NO SALTAMOS LOS TESTS!
                // Ahora mvn intentará arrancar la app y conectarse a la BD.
                sh 'mvn clean package' 
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
