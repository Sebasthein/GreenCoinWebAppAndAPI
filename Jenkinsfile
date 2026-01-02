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
