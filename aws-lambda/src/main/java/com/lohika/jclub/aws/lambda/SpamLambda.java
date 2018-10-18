package com.lohika.jclub.aws.lambda;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;

public class SpamLambda {
    //    SnsClient snsClient = SnsClient.builder()
//            .region(Region.EU_WEST_1)
//            .build();
    AmazonSNS snsClient = AmazonSNSClient.builder()
            .withRegion(Regions.EU_WEST_1)
            .build();


    public void handleRequest(DynamodbEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("---------------------- " + event.getClass());
        logger.log("---------------------- " + event.toString());

        event.getRecords().stream()
                .filter(r -> r.getEventName().equals("MODIFY"))
                .forEach(r -> {
//                            PublishRequest request = PublishRequest.builder()
//                                    .message("Zalyshok: " + r.getDynamodb().getNewImage().get("deposit").getN())
////                                    .phoneNumber("+380631773525")
////                                    .topicArn("arn:aws:sns:eu-west-1:713791747198:java_club_topic_sms")
//                                    .targetArn("arn:aws:sns:eu-west-1:713791747198:java_club_topic_sms:0abf1fcb-e708-42e0-8f98-150002ace035")
//                                    .build();
                            logger.log("************************************************************************");
                            Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
                            smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                                    .withStringValue("mySenderID") //The sender ID shown on the device.
                                    .withDataType("String"));
                            smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                                    .withStringValue("Promotional") //Sets the type to promotional.
                                    .withDataType("String"));

                            PublishResult publishResponse = snsClient.publish(new PublishRequest()
                                    .withMessage("Zalyshok: " + r.getDynamodb().getNewImage().get("deposit").getN())
//                                    .withPhoneNumber("+380631773525")
                                    .withTopicArn("arn:aws:sns:eu-west-1:713791747198:java_club_topic_sms")
                                    .withMessageAttributes(smsAttributes)
                            );
                            //PublishResponse publishResponse = snsClient.publish(request);

                            logger.log("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//                            logger.log("Message ID: " + publishResponse.messageId());
//                            logger.log("Response: " + publishResponse.toString());
                            logger.log("Message ID: " + publishResponse.getMessageId());
                            logger.log("Response Code: " + publishResponse.getSdkHttpMetadata().getHttpStatusCode());
                            logger.log("Response Headers: " + publishResponse.getSdkHttpMetadata().getHttpHeaders());
                            logger.log("Response: " + publishResponse.toString());

                        }
                );
    }
}
