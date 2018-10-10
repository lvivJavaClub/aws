package com.lohika.jclub.aws.lambda;

import com.amazonaws.kinesis.deagg.RecordDeaggregator;
import com.amazonaws.monitoring.CsmConfigurationProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.lohika.jclub.aws.model.Vehicle;

import java.util.UUID;

public class UpdateBalanceLambda implements RequestHandler<KinesisEvent, Void> {
    final private AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    final private DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    final private Table vehiclesTable = dynamoDB.getTable("java_club_vehicles");
    DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

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

            Vehicle vehicle = mapper.load(Vehicle.class, eventItem.get("vrn"));

            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey(new PrimaryKey("vrn", eventItem.get("vrn")))
                    .withUpdateExpression("ADD deposit :depo")
                    .withValueMap(new ValueMap().withNumber(":depo", -vehicle.getPlan().getPerEntranceWithdrawalAmount()))
                    .withReturnValues(ReturnValue.UPDATED_NEW);

            UpdateItemOutcome result = vehiclesTable.updateItem(updateItemSpec);

            logger.log(result.getUpdateItemResult().toString());
        });

        return null;
    }

}
