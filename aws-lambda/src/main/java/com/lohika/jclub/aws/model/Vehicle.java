package com.lohika.jclub.aws.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName="java_club_vehicles")
@Getter
@Setter
public class Vehicle {

    @DynamoDBHashKey(attributeName="vrn")
    private String vrn;

    @DynamoDBAttribute(attributeName="deposit")
    public long balance;

    @DynamoDBTypeConvertedEnum
    // Equivalent:
    //    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute(attributeName="plan")
    public Plan plan;

    @DynamoDBAttribute(attributeName="vin")
    public String vin;

}
