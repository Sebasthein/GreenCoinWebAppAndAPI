pipeline {
    agent any
    
    tools {
        maven 'Maven-3' 
        jdk 'Java-21' 
    }

    stages {
        stage('Holaaa, Verificar Versiones 🧐') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }
        
        stage('Construir sin Tests 🔨') {
            steps {
                echo 'Compilando código y empaquetando...'
                // -DskipTests: La clave para que no intente conectar a la BD
                sh 'mvn clean package -DskipTests' 
            }
        }
    }
    
    post {
        success {
            echo '¡Empaquetado exitoso! Guardando el .jar...'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}
