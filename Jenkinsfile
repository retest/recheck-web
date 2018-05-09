pipeline {
	agent {
		label 'linux'
	}

	options {
		timeout(time: 5, unit: 'MINUTES')
		buildDiscarder(logRotator(numToKeepStr: '10'))
	}

	tools {
		maven 'Default'
		jdk 'Default'
	}

	stages {
		stage('Build') {
			steps {
				sh 'mvn clean compile test-compile'
			}
		}

		stage('Tests') {
			steps {
				sh 'mvn test'
			}
		}
	}
}
