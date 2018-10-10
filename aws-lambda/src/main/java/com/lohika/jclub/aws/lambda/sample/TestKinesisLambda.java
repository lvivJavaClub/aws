package com.lohika.jclub.aws.lambda.sample;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.SdkBytes;

import java.nio.charset.Charset;
import java.util.Map;

//import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
//import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
//import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

@Slf4j
public class TestKinesisLambda {
    private LambdaLogger logger;
    final private AmazonKinesis kinesisClient = AmazonKinesisClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1.getName()).build(); //defaultClient();

    public PutRecordsResult write(String event, Context context) {

//        System.getenv().entrySet().stream().forEach(e -> log.info("{} = {}", e.getKey(), e.getValue()));
//        log.info("AWS_ACCESS_KEY_ID = {}", System.getenv("AWS_ACCESS_KEY_ID"));
//        log.info("AWS_SECRET_KEY = {}", System.getenv("AWS_SECRET_KEY"));

//        try {
//            PutRecordRequest putRecordRequest = PutRecordRequest.builder()
//                    .data(SdkBytes.fromString(event, Charset.defaultCharset()))
//                    .partitionKey("some key")
//                    .streamName("java_club_stream")
//                    .build();
//
//            log.info("Creating client....");
//
//            KinesisAsyncClient client = KinesisAsyncClient.builder()
//                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
//                            "<AK_ID>",
//                            "<SEC_K>"
//                    )))
//                    .region(Region.EU_CENTRAL_1)
//                    .build();
//            PutRecordResponse putRecordResponse = client.putRecord(putRecordRequest).get();
//            log.info(putRecordResponse.toString());
//        } catch (Exception e) {
//            return e.getMessage();
//        }

        // set up the client
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest().withStreamName("java_club_stream");
        putRecordsRequest.withRecords(
                new PutRecordsRequestEntry().withData(SdkBytes.fromString("Sample kinesis event1" + event, Charset.defaultCharset()).asByteBuffer()).withPartitionKey("some key"),
                new PutRecordsRequestEntry().withData(SdkBytes.fromString("Sample kinesis event2" + event, Charset.defaultCharset()).asByteBuffer()).withPartitionKey("some key")
        );

        PutRecordsResult result = kinesisClient.putRecords(putRecordsRequest);
        //return "OK: " + event;
        return result;
    }
}
