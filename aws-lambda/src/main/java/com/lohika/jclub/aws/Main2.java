package com.lohika.jclub.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.utils.FunctionalUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main2 implements RequestHandler<Main2.Vehicle, String> {

    public static class Vehicle {
        public String vin;
        public String color;

        @Override
        public String toString() {
            return "Vehicle{" +
                    "vin='" + vin + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    @Override
    public String handleRequest(Vehicle vehicle, Context context) {
        //final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
//        S3AsyncClient client = S3AsyncClient.create();
//        CompletableFuture<ListBucketsResponse> listBuckets = client.listBuckets();

//        try {
//            return listBuckets.whenComplete(
//                    (resp, err) -> {
//                        try {
//                            if (resp != null) {
//                                //System.out.println(resp);
//                            } else {
//                                err.printStackTrace();
//                            }
//                        } finally {
//                            FunctionalUtils.invokeSafely(client::close);
//                        }
//                    }
//            ).get().buckets().stream().map(Bucket::name).collect(Collectors.joining("; "));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        return vehicle.toString();
    }
}