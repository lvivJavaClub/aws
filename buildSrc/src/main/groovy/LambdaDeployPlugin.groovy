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

        project.task('deployLambda') {

            group = 'AWS Lambda Provisioning'
            description = 'AWS Lambda Function Deployment Task'

            doLast {
                def jar = new File(Paths.get(project.lambdaBinaryDir, "${project.name}.jar").toString())
                def key = "java_club_lambda/0.1.jar"

                println "Deploying ${jar} to ${project.lambdaS3BucketName} bucket for ${project.lambdaName} lambda function"

                def zip = new PutObjectRequest(project.lambdaS3BucketName, key, jar)

                def s3Client = new AmazonS3ClientBuilder().withRegion(Regions.EU_CENTRAL_1).build()
                s3Client.putObject zip

                def request = new UpdateFunctionCodeRequest()
                        .withFunctionName(project.lambdaName)
                        .withS3Bucket(project.lambdaS3BucketName)
                        .withS3Key(key)

                def lambdaClient = new AWSLambdaClientBuilder().withRegion(Regions.EU_CENTRAL_1).build()
                lambdaClient.updateFunctionCode request
            }
        }
    }

}