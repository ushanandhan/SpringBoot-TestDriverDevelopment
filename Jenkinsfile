pipeline{
	agent any
	tools { 
        	jdk 'Java8'
		maven 'Maven_3.5.4'
    	}
	stages{
		stage('Compile Stage'){
			steps{
				bat 'mvn clean compile'
			}
		}
		
		stage('Test Stage'){
			steps{
				bat 'mvn test'
			}
		}
	}
	post{
		always{
			echo 'This Will Always Run'
		}
		success{
			echo 'This will execute only the pipeline is success'
		}
		failure{
			echo 'This will execute only the pipeline is failed'
		}
		unstable{
			echo 'This will execute only if pipeline ran partially'
		}
		changed{
			echo 'This will execute if any status changed from previous build'
		}
	}
}
