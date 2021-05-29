package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.StartJobRunRequest;
import com.amazonaws.services.glue.model.StartJobRunResult;

import java.util.HashMap;
import java.util.Map;

public class StartJobRunRequestTester {

    public static void main(String[] args) throws Exception {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("ADMIN", "admin123");

        AwsClientBuilder.EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration(EndPoint.getEndPoint(), "korea");

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(0); // 0로 하지 않으면 여러번 호출한다.

        AWSGlue glue = AWSGlueClient.builder()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(configuration)
                .build();

        //////////////////////////////////////////////////
        StartJobRunRequest request = new StartJobRunRequest();
        request.setJobName("PY_PRINT");
        Map<String, String> argMap = new HashMap<>();
        argMap.put("first1", "kang1");
        argMap.put("second2", "jung2");
        argMap.put("third3", "hwa3");
        request.setArguments(argMap);
        StartJobRunResult result = glue.startJobRun(request);

        System.out.println(result.getJobRunId());
        //////////////////////////////////////////////////
    }
}
