package com.lgdisplay.bigdata.api.service.glue.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateTriggerRequestTester {

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
        UpdateTriggerRequest request = new UpdateTriggerRequest();
        TriggerUpdate update = new TriggerUpdate();

        request.setName("trigger6");

        List<Action> actionList = new ArrayList<>();

        Action action = new Action();
        Map<String, String> updateMap = new HashMap<>();
        updateMap.put("update_test","100");
        NotificationProperty notificationProperty = new NotificationProperty();
        notificationProperty.setNotifyDelayAfter(1*1000*9);
        action.setArguments(updateMap);
        action.setJobName("UpdateJobName");
        action.setNotificationProperty(notificationProperty);

        Action action2 = new Action();
        Map<String, String> startOnCreate2 = new HashMap<>();
        startOnCreate2.put("update_test2","200");
        NotificationProperty notificationProperty2 = new NotificationProperty();
        notificationProperty2.setNotifyDelayAfter(1*1000*3);
        action2.setArguments(startOnCreate2);
        action2.setJobName("UpdateJobName2");
        action2.setNotificationProperty(notificationProperty2);

        actionList.add(action);
        actionList.add(action2);

        update.setActions(actionList);
        update.setDescription("update test");
        update.setName("update trigger name");
        update.setSchedule("cron(10 * * * *)");
        Predicate predicate = new Predicate();
        update.setPredicate(predicate);
        request.setTriggerUpdate(update);

        UpdateTriggerResult result = glue.updateTrigger(request);
        System.out.println(result.getTrigger().getName());
        //////////////////////////////////////////////
    }

}
