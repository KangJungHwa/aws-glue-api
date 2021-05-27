# CreateTrigger

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_CreateTrigger.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.create_Trigger

### Request 정리
* TriggerName은 필수 (1~255 길이)
  * TriggerName은은 Unique 해야 합니다.
* 다른 사용자의 JOB을 이용하여 Trigger를 구성할 수 있습니다.
  
* Type 태그의 CodeList 종류
   * SCHEDULED, ON_DEMAND
     * Type이 SCHEDULED 인 경우 : 
        * Schedule에 CRON 표현식이 존재해야 합니다.
        * StartOnCreation이 TRUE 인 경우 스케줄이 바로 시작됩니다.
          * Action 태그내의 job이 순차적으로 실행됩니다.
            * 실행과 동시에 jobRun 테이블에 실행 이력이 기록됩니다. 
          * 실행시 scriptLocation에 script가 존재하지 않으면 에러가 발생합니다.
        * StartOnCreation이 FALSE 인 경우 스케줄이 STANDBY 상태 입니다.
          * 추후에 StartTrigger로 스케줄을 시작 할 수 있습니다.  
     * Type이 ON_DEMAND 인 경우 :
        * StartOnCreation이 true가 입력 될 수 없습니다.
        * Schedule에 CRON 표현식을 입력할 수 없습니다.
     
* Actions 안에 포함된 jobName은 모두 job 테이블에 모두 존재 해야 합니다.

```json
{
   "Actions": [ 
      { 
         "Arguments": { 
            "string" : "string" 
         },
         "CrawlerName": "string",
         "JobName": "string",
         "NotificationProperty": { 
            "NotifyDelayAfter": number
         },
         "SecurityConfiguration": "string",
         "Timeout": number
      }
   ],
   "Description": "string",
   "Name": "string",
   "Predicate": { 
      "Conditions": [ 
         { 
            "CrawlerName": "string",
            "CrawlState": "string",
            "JobName": "string",
            "LogicalOperator": "string",
            "State": "string"
         }
      ],
      "Logical": "string"
   },
   "Schedule": "string",
   "StartOnCreation": boolean,
   "Tags": { 
      "string" : "string" 
   },
   "Type": "string",
   "WorkflowName": "string"
}
```

Python 호출코드입니다.

```python
response = client.create_trigger(
    Name='string',
    WorkflowName='string',
    Type='SCHEDULED'|'CONDITIONAL'|'ON_DEMAND',
    Schedule='string',
    Predicate={
        'Logical': 'AND'|'ANY',
        'Conditions': [
            {
                'LogicalOperator': 'EQUALS',
                'JobName': 'string',
                'State': 'STARTING'|'RUNNING'|'STOPPING'|'STOPPED'|'SUCCEEDED'|'FAILED'|'TIMEOUT',
                'CrawlerName': 'string',
                'CrawlState': 'RUNNING'|'CANCELLING'|'CANCELLED'|'SUCCEEDED'|'FAILED'
            },
        ]
    },
    Actions=[
        {
            'JobName': 'string',
            'Arguments': {
                'string': 'string'
            },
            'Timeout': 123,
            'SecurityConfiguration': 'string',
            'NotificationProperty': {
                'NotifyDelayAfter': 123
            },
            'CrawlerName': 'string'
        },
    ],
    Description='string',
    StartOnCreation=True|False,
    Tags={
        'string': 'string'
    }
)
```

Java 호출 코드입니다.

```java
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

        Action action = new Action();
        Map<String, String> argument1 = new HashMap<>();
        argument1.put("DATE","2020-02-02");
        argument1.put("TYPE","PYTHON");
        NotificationProperty notificationProperty = new NotificationProperty();
        notificationProperty.setNotifyDelayAfter(1*1000*5);
        action.setArguments(argument1);
        action.setJobName("PY_PRINT");
        action.setNotificationProperty(notificationProperty);


        Action action2 = new Action();
        Map<String, String> argument2 = new HashMap<>();

        argument2.put("DATE2","2020-02-02");
        argument2.put("TYPE2","PYTHON");
        NotificationProperty notificationProperty2 = new NotificationProperty();
        notificationProperty2.setNotifyDelayAfter(1*1000*5);

        action2.setArguments(argument2);
        action2.setJobName("PY_PRINT1");
        action2.setNotificationProperty(notificationProperty2);


        actionList.add(action);
        actionList.add(action2);
        request.setActions(actionList);
        request.setSchedule("0/3 * * * * ?");
        request.setType("SCHEDULED");
        request.setStartOnCreation(true);
//        request.setType("ON_DEMAND");
//        request.setStartOnCreation(false);
        request.setDescription("test trigger");

        request.setName("TRIGGER");
        CreateTriggerResult result = glue.createTrigger(request);
        System.out.println(result.getName());
        //////////////////////////////////////////////////
    }
}
```

## Response 정리

* AlreadyExistsException (400)
* InvalidInputException (400)
* InternalServiceException (500)

```
{
   "Name": "string"
}
```
