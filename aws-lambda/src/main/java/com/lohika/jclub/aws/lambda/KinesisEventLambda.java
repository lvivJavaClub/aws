package com.lohika.jclub.aws.lambda;

import com.amazonaws.kinesis.deagg.RecordDeaggregator;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

import java.util.UUID;

public class KinesisEventLambda implements RequestHandler<KinesisEvent, Void> {
    final private AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    final private DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    final private Table logEventsTable = dynamoDB.getTable("incomingEvents");

    @Override
    public Void handleRequest(KinesisEvent event, Context context) {
        LambdaLogger logger = context.getLogger();

        if (event.getRecords() != null) {
            logger.log("Received " + event.getRecords().size() + " raw Event Records. ");
        } else {
            logger.log("Received no records");
            return null;
        }


        // Stream the User Records from the Lambda Event
        RecordDeaggregator.stream(event.getRecords().stream(), userRecord -> {
            String data = new String(userRecord.getData().array());
            logger.log(data);

            Item eventItem = Item.fromJSON(data)
                    .withPrimaryKey("eventId", UUID.randomUUID().toString());
            PutItemOutcome result = logEventsTable.putItem(eventItem);

            logger.log(result.toString());
        });

        // the same:
        //event.getRecords().forEach(userRecord -> logger.log(new String(userRecord.getKinesis().getData().array())));

        return null;
    }
}
