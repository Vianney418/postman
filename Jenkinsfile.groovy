pipeline {
    agent any

    tools{
        nodejs 'node'
    }

    stages {

        stage('Preparation') {
            steps {
                script {
                    sh 'rm -f results/TEST-postman-results.xml'
                    
                }
            }
        }

        stage('Run Postman Tests') {
            steps { 
                script{
                    //sh 'npm install newman@5.2.3 newman-reporter-htmlextra@1.19.0'
                    sh 'npx newman run tests\API_Prueba.postman_collection.json -e enviroment\testpostman.postman_environment.json --reporters cli,junit,htmlextra --reporter-junit-export results/TEST-postman-results.xml --reporter-htmlextra-export results/TEST-postman-results.html --suppress-exit-code'
                      
                }
            }
        }

        stage('Publish HTML Report') {
            steps {
                publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'results',
                        reportFiles: 'TEST-postman-results.html',
                        reportName: 'Postman Report'
                ])
            }
        }

    }

    post {
        always {
            archiveArtifacts 'results/*.xml'
        }
    }

}