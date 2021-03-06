package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.CreateJobRequest;
import com.amazonaws.services.glue.model.CreateJobResult;
import com.amazonaws.services.glue.model.JobCommand;

public class CreateJobRequestTester {

    public static void main(String[] args) throws Exception {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("admin", "admin123");

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
        CreateJobRequest request = new CreateJobRequest();
        JobCommand command = new JobCommand();
        command.setName("PYTHON");
        command.setPythonVersion("3.7.1");



        command.setScriptLocation("print_alpha.py");
        request.setCommand(command);
        request.setName("PY_PRINT");

//        command.setScriptLocation("print_han_alpha.py");
//        request.setCommand(command);
//        request.setName("PY_PRINT1");
//
//        command.setScriptLocation("print_number.py");
//        request.setCommand(command);
//        request.setName("PY_PRINT2");
//
//        command.setScriptLocation("print_han_number.py");
//        request.setCommand(command);
//        request.setName("PY_PRINT3");

        CreateJobResult result = glue.createJob(request);
        System.out.println(result.getName());
        //////////////////////////////////////////////////
    }
}
