pipeline {
    agent { label 'ams-02' }
    
    stages {
        stage('Announce') {
            steps {
                echo 'This is the NetRexx multi-branch build'
		sh 'pwd'
            }
        }
       	stage('Build') {
            steps {
	           sh 'make'
                   }
        }

    }
}
