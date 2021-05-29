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

        AwsClientBuilder.EndpointConfiguration configuration =
                new AwsClientBuilder.EndpointConfiguration(EndPoint.getEndPoint(), "korea");

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
        request.setName("TRIGGER");

        List<Action> actionList = new ArrayList<>();

        Action action = new Action();
        Map<String, String> updateMap = new HashMap<>();
        action.setJobName("PY_PRINT");
        Action action2 = new Action();
        action2.setJobName("PY_PRINT1");

        actionList.add(action);
        actionList.add(action2);

        update.setActions(actionList);
        update.setDescription("update test");
        update.setSchedule("0/45 * * * * ?");

        update.setDescription("test trigger");
        request.setTriggerUpdate(update);

        UpdateTriggerResult result = glue.updateTrigger(request);
        System.out.println(result.getTrigger().getName());
        //////////////////////////////////////////////
    }

}
