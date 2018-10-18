package com.lohika.jclub.aws.lambda;

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
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.lohika.jclub.aws.model.Vehicle;

import java.util.Map;
import java.util.UUID;

public class ScheduledUpdateBalanceLambda implements RequestHandler<Map, Void> {
    final private AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    private final DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    private final Table vehiclesTable = dynamoDB.getTable("java_club_vehicles");

    // https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html
    private final DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

    @Override
    public Void handleRequest(Map event, Context context) {
        LambdaLogger logger = context.getLogger();
        //logger.log("====================== Event type: " + event);
        event.entrySet().forEach(e -> logger.log(e.toString()));
        return null;
    }

}
