package com.lohika.jclub.aws.lambda;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lohika.jclub.aws.model.IncomingEvent;
import com.lohika.jclub.aws.util.GateUtils;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.model.LambdaException;

import java.nio.charset.Charset;


@Slf4j
public class InboundEventLambda {
    final private AmazonKinesis kinesisClient = AmazonKinesisClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    final private AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    private final DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    private final Table vehiclesTable = dynamoDB.getTable("java_club_vehicles");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public PutRecordResult write(IncomingEvent event, Context context) throws JsonProcessingException {
        context.getLogger().log("Accepting incoming event from API GW");

        String serializedEvent = objectMapper.writeValueAsString(event);
        context.getLogger().log("Event: : " + serializedEvent);

        validateGateId(event);

        validateVrn(event);

        // set up the client
        PutRecordRequest putRecordRequest = new PutRecordRequest()
                .withStreamName("java_club_stream")
                .withData(SdkBytes.fromString(serializedEvent, Charset.defaultCharset()).asByteBuffer())
                // set key to evenly distribute events among shards
                .withPartitionKey(event.getVrn());

        return kinesisClient.putRecord(putRecordRequest);
    }

    private void validateVrn(IncomingEvent event) {
        Item vehiclesTableItem = vehiclesTable.getItem(new PrimaryKey("vrn", event.getVrn()));

        if (vehiclesTableItem == null) {
            throw LambdaException.builder()
                    .message("Unregistered vehicle: " + event.getVrn())
                    .statusCode(401)
                    .build();
        }
    }

    private void validateGateId(IncomingEvent event) {
        if (!GateUtils.isValid(event.getGateId())) {
            throw LambdaException.builder()
                    .message("Invalid Gate ID: " + event.getGateId())
                    .statusCode(400)
                    .build();
        }
    }
}
