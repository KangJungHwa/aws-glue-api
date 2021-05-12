package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.ListJobsRequest;
import com.amazonaws.services.glue.model.ListJobsResult;
import com.amazonaws.services.glue.model.ListTriggersRequest;
import com.amazonaws.services.glue.model.ListTriggersResult;

public class ListTriggersRequestTester {

    public static void main(String[] args) throws Exception {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("admin", "admin123");

        AwsClientBuilder.EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8888/glue", "korea");

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(0); // 0로 하지 않으면 여러번 호출한다.

        AWSGlue glue = AWSGlueClient.builder()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(configuration)
                .build();

        //////////////////////////////////////////////
        ListTriggersRequest request = new ListTriggersRequest();
        request.setMaxResults(2);
        request.setDependentJobName("trigger9");

        ListTriggersResult result = glue.listTriggers(request);
        System.out.println(result);
        //////////////////////////////////////////////
    }
}