import com.amazonaws.regions.Regions
import com.amazonaws.services.lambda.AWSLambdaClientBuilder
import com.amazonaws.services.lambda.model.UpdateFunctionCodeRequest
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.PutObjectRequest
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Paths

class LambdaDeployPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.task('deployLambdas') {
            group = 'AWS Lambda Provisioning'
            description = 'AWS Lambda Function Deployment Task'

            doLast {
                def s3key = "java_club_lambda/0.1.jar"

                def jar = new File(Paths.get(project.lambdaJarDir, "${project.name}.jar").toString())
                uploadJarToS3(jar, project.lambdaS3BucketName, s3key)

                project.lambdaNames.each {
                    updateLambda(project.lambdaS3BucketName, s3key, it)
                }

            }
        }
    }

    def uploadJarToS3(jar, lambdaS3BucketName, s3key) {
        println "Deploying ${jar} to ${lambdaS3BucketName} bucket"
        def zip = new PutObjectRequest(lambdaS3BucketName, s3key, jar)

        def s3Client = new AmazonS3ClientBuilder().withRegion(Regions.EU_CENTRAL_1).build()
        s3Client.putObject zip
    }

    def updateLambda(lambdaS3BucketName, s3key, lambdaName) {
        println "Reloading ${lambdaName} lambda function"
        def request = new UpdateFunctionCodeRequest()
                .withFunctionName(lambdaName)
                .withS3Bucket(lambdaS3BucketName)
                .withS3Key(s3key)

        def lambdaClient = new AWSLambdaClientBuilder().withRegion(Regions.EU_CENTRAL_1).build()
        lambdaClient.updateFunctionCode request
    }
}