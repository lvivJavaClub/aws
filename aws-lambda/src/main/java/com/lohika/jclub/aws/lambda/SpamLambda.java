package com.lohika.jclub.aws.lambda;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class SpamLambda {
    //    SnsClient snsClient = SnsClient.builder()
//            .region(Region.EU_WEST_1)
//            .build();
    AmazonSNS snsClient = AmazonSNSClient.builder()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();


    public void handleRequest(DynamodbEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("---------------------- " + event.getClass());
        logger.log("---------------------- " + event.toString());

        event.getRecords().stream()
                .filter(r -> r.getEventName().equals("MODIFY"))
                .forEach(r -> {
                            //Create Topic
                            String topicArn = "arn:aws:sns:eu-central-1:713791747198:java_club_topic";
                            SubscribeRequest subscribeRequest = new SubscribeRequest(
                                    topicArn,
                                    "email",
                                    "customEmail@example.com"
                            );
                            snsClient.subscribe(subscribeRequest);

                            //Send email
                            PublishRequest publishRequest = new PublishRequest();
                            publishRequest.setMessage("Email test message");
                            publishRequest.setTopicArn(topicArn);
                            publishRequest.setSubject("Test Subject");
                            PublishResult publish = snsClient.publish(publishRequest);
                        }
                );
    }

}
