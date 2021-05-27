package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.JobCommand;
import com.amazonaws.services.glue.model.JobUpdate;
import com.amazonaws.services.glue.model.UpdateJobRequest;
import com.amazonaws.services.glue.model.UpdateJobResult;

public class UpdateJobRequestTester {

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
        UpdateJobRequest request = new UpdateJobRequest();
        JobUpdate update = new JobUpdate();
        JobCommand command = new JobCommand();

        command.setName("PYTHON");
        command.setPythonVersion("3.7.1~~~~~");
        command.setScriptLocation("print_alphabet.py");

        update.setCommand(command);
        request.setJobName("PY_PRINT4");
        request.setJobUpdate(update);

        UpdateJobResult result = glue.updateJob(request);
        System.out.println(result.getJobName());
        //////////////////////////////////////////////
    }

}
