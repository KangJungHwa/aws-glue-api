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
import com.amazonaws.services.identitymanagement.model.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateJobRequestTester {

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


        CreateJobRequest request = new CreateJobRequest();
        JobCommand command = new JobCommand();
        command.setName("test");
        command.setPythonVersion("3.7.1");
        command.setScriptLocation("s3:/test.py");
        request.setCommand(command);
        request.setName("ExampleJob");

        CreateJobResult result = glue.createJob(request);

        System.out.println(result.getName());
    }

    public static Collection<Tag> toTags(Object... objects) {
        List list = new ArrayList();
        Tag tag1 = new Tag();
        tag1.setKey("name");
        tag1.setValue("hong");
        list.add(tag1);

        Tag tag2 = new Tag();
        tag2.setKey("depart");
        tag2.setValue("C101");
        list.add(tag2);

        return list;
    }

}
