package com.lohika.jclub.aws.lambda.sample;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class VerySimpleCounterLambda {

    public String handleRequest2(Integer myCount, Context context) {
        return String.valueOf(myCount);
    }
}