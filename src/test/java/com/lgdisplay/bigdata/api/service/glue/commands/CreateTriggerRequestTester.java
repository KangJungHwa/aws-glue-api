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
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("admin", "admin123");

        AwsClientBuilder.EndpointConfiguration configuration = new AwsClientBuilder.EndpointConfiguration("http://localhost:8888/glue", "korea");

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(0); // 0로 하지 않으면 여러번 호출한다.

        AWSGlue glue = AWSGlueClient.builder()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(configuration)
                .build();

        //////////////////////////////////////////////////
        CreateTriggerRequest request = new CreateTriggerRequest();

        request.setName("trigger6");
        request.setWorkflowName("WorkflowName1");

        List<Action> actionList = new ArrayList<>();
        request.setSchedule("cron(10 * * * *)");
        Action action = new Action();
        Map<String, String> startOnCreate = new HashMap<>();
        startOnCreate.put("startOnCreate1","1");
        NotificationProperty notificationProperty = new NotificationProperty();
        notificationProperty.setNotifyDelayAfter(1*1000*5);
        action.setArguments(startOnCreate);
        action.setJobName("testJobName");
        action.setNotificationProperty(notificationProperty);


        Action action2 = new Action();
        Map<String, String> startOnCreate2 = new HashMap<>();
        startOnCreate2.put("startOnCreate2","2");
        NotificationProperty notificationProperty2 = new NotificationProperty();
        notificationProperty2.setNotifyDelayAfter(1*1000*5);
        action2.setArguments(startOnCreate2);
        action2.setJobName("testJobName2");
        action2.setNotificationProperty(notificationProperty2);


        actionList.add(action);
        actionList.add(action2);
        request.setActions(actionList);
        request.setType("MatLap");
        request.setDescription("test trigger");
        request.setStartOnCreation(false);
        CreateTriggerResult result = glue.createTrigger(request);
        System.out.println(result.getName());
        //////////////////////////////////////////////////
    }
}
