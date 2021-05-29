package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTriggerRequestTester {

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
        CreateTriggerRequest request = new CreateTriggerRequest();

        request.setWorkflowName("WorkflowName6");

        List<Action> actionList = new ArrayList<>();

        Action action = new Action();
        action.setJobName("PY_PRINT2");

        Action action2 = new Action();
        action2.setJobName("PY_PRINT3");

        actionList.add(action);
        actionList.add(action2);
        request.setActions(actionList);
        request.setSchedule("0/10 * * * * ?");
        request.setType("SCHEDULED");
        request.setStartOnCreation(true);
//        request.setType("ON_DEMAND");
//        request.setStartOnCreation(false);
        request.setDescription("test trigger");

        request.setName("TRIGGER1");
        CreateTriggerResult result = glue.createTrigger(request);
        System.out.println(result.getName());
        //////////////////////////////////////////////////
    }
}
