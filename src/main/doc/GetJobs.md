# GetJobs

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_GetJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.get_jobs
### Request 정리
* 모든 사용자이 JOB을 조회할 수 있습니다.

```json
{
  "MaxResults": number,
  "NextToken": "string"
}
```

Python 호출코드입니다.

```python
response = client.get_jobs(
    MaxResults=3
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
```
## Response 정리

* InternalServiceException (500)
* EntityNotFoundException (400)
* InvalidInputException (400)

```
{
   "Jobs": [ 
      { 
         "AllocatedCapacity": number,
         "Command": { 
            "Name": "string",
            "PythonVersion": "string",
            "ScriptLocation": "string"
         },
         "Connections": { 
            "Connections": [ "string" ]
         },
         "CreatedOn": number,
         "DefaultArguments": { 
            "string" : "string" 
         },
         "Description": "string",
         "ExecutionProperty": { 
            "MaxConcurrentRuns": number
         },
         "GlueVersion": "string",
         "LastModifiedOn": number,
         "LogUri": "string",
         "MaxCapacity": number,
         "MaxRetries": number,
         "Name": "string",
         "NonOverridableArguments": { 
            "string" : "string" 
         },
         "NotificationProperty": { 
            "NotifyDelayAfter": number
         },
         "NumberOfWorkers": number,
         "Role": "string",
         "SecurityConfiguration": "string",
         "Timeout": number,
         "WorkerType": "string"
      }
   ],
   "NextToken": "string"
}
```
