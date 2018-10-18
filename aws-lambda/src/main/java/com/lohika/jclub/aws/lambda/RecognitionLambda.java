package com.lohika.jclub.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

public class RecognitionLambda {

    private static RekognitionClient rekognitionClient = RekognitionClient.builder()
            .region(Region.EU_WEST_1)
            .build();

    public void handleRequest(S3Event s3Event, Context context) {
        LambdaLogger logger = context.getLogger();
        s3Event.getRecords().forEach(e -> {
            S3EventNotification.S3Entity s3 = e.getS3();
            String name = s3.getBucket().getName();
            String key = s3.getObject().getKey();
            logger.log("S3 object bucket name: " + name);
            logger.log("S3 object key " + key);

            handleRequest(rekognitionClient, name, key);
        });

    }

    public static void main(String[] args) {
        handleRequest(rekognitionClient, "java-club-license-plate-images2", "car9_ass.jpg");
    }

    private static void handleRequest(RekognitionClient rekognitionClient, String bucket, String imageName) {
        Image image = Image.builder()
                .s3Object(S3Object.builder()
                        .bucket(bucket)
                        .name(imageName)
                        .build())
                .build();
        DetectTextResponse detectTextResponse = rekognitionClient
                .detectText(DetectTextRequest.builder()
                        .image(image)
                        .build()
                );
        detectTextResponse.textDetections().forEach(t -> {
            System.out.println("Detected text: " + t.toString());
        });
    }

}
