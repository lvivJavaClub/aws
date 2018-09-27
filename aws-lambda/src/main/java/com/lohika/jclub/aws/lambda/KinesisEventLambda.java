package com.lohika.jclub.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.kinesis.deagg.RecordDeaggregator;

public class KinesisEventLambda implements RequestHandler<KinesisEvent, Void> {

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
            logger.log(new String(userRecord.getData().array()));
        });

        // the same:
        //event.getRecords().forEach(userRecord -> logger.log(new String(userRecord.getKinesis().getData().array())));

        return null;
    }
}
