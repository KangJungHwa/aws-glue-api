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

        request.setWorkflowName("WorkflowName6");

        List<Action> actionList = new ArrayList<>();
        request.setSchedule("0/3 * * * * ?");

        Action action = new Action();
        Map<String, String> argument1 = new HashMap<>();
        argument1.put("DATE","2020-02-02");
        argument1.put("TYPE","PYTHON");
        NotificationProperty notificationProperty = new NotificationProperty();
        notificationProperty.setNotifyDelayAfter(1*1000*5);
        action.setArguments(argument1);
        action.setJobName("PY_SAMPLE1");
        action.setNotificationProperty(notificationProperty);


        Action action2 = new Action();
        Map<String, String> argument2 = new HashMap<>();

        argument2.put("DATE2","2020-02-02");
        argument2.put("TYPE2","PYTHON");
        NotificationProperty notificationProperty2 = new NotificationProperty();
        notificationProperty2.setNotifyDelayAfter(1*1000*5);

        action2.setArguments(argument2);
        action2.setJobName("PY_SAMPLE2");
        action2.setNotificationProperty(notificationProperty2);


        actionList.add(action);
        actionList.add(action2);
        request.setActions(actionList);
        request.setType("MatLap");

        request.setDescription("test trigger");
        request.setStartOnCreation(true);
        request.setName("TRIGGER5");
        CreateTriggerResult result = glue.createTrigger(request);
        System.out.println(result.getName());
        //////////////////////////////////////////////////
    }
}
