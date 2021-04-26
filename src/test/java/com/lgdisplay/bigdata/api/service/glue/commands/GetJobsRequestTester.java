package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.GetJobsRequest;
import com.amazonaws.services.glue.model.GetJobsResult;

public class GetJobsRequestTester {

    public static void main(String[] args) throws Exception {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("admin", "admin123");

        AwsClientBuilder.EndpointConfiguration configuration = new AwsClientBuilder.EndpointConfiguration("http://localhost:8888/glue", "korea");

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(0); // 0로 하지 않으면 여러번 호출한다.

        AWSGlue glue = AWSGlueClient.builder()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(configuration)
                .build();

        //////////////////////////////////////////////
        GetJobsRequest request = new GetJobsRequest();
        request.setMaxResults(3);

        GetJobsResult result = glue.getJobs(request);
        System.out.println(result);
        for (int i = 0; i < result.getJobs().size(); i++) {
            System.out.println(result.getJobs().get(i).getName());
            System.out.println(result.getJobs().get(i).getCommand().getName());
            System.out.println(result.getJobs().get(i).getCommand().getScriptLocation());
            System.out.println(result.getJobs().get(i).getCommand().getPythonVersion());
        }
        //////////////////////////////////////////////
    }


}
