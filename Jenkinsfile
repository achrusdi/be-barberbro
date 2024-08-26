pipeline {
    agent any
    // agent {
    //     node {
    //         label 'linux && java21'
    //     }
    // }

    stages {
        // stage('Checkout') {
        //     steps {
        //         git branch: 'main', url: 'https://github.com/achrusdi/jenkins-test.git'
        //     }
        // }

        // stage('Verify Tools') {
        //     steps {
        //         sh '''
        //             docker version
        //             docker info
        //             docker compose version
        //         '''
        //     }
        // }
        


        stage('Build') {
            steps {
                echo 'Building the project...'
            }
        }

        // stage('Docker Compose Up') {
        //     steps {
        //         script {
        //             // Menjalankan docker-compose down untuk menghentikan dan menghapus container lama
        //             sh 'docker-compose down'

        //             // Menjalankan docker-compose up dengan opsi --build untuk membangun ulang image dan menjalankan container
        //             sh 'docker-compose up --build -d'
        //         }
        //     }
        // }

        stage('Test') {
            steps {
                echo 'Hello Test'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Hello Deploy'
            }
        }
    }

    post {
        always {
            echo 'This will always run'
        }

        success {
            echo 'This will run only if successful'
        }

        failure {
            echo 'This will run only if failed'
        }

        cleanup {
            echo 'This will always run cleanup'
        }
    }
}
