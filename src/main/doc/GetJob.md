# GetJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_GetJob.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.delete_job

### Request 정리

* JobName 필수 (1~255 길이)

```json
{
  "JobName": "string"
}
```

Python 호출코드입니다.

```python
response = client.get_job(
    JobName='string'
)
```

Java 호출 코드입니다.

```java
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.GetJobRequest;
import com.amazonaws.services.glue.model.GetJobResult;

public class GetJobRequestTester {

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
        GetJobRequest request = new GetJobRequest();
        request.setJobName("ExampleJob");

        GetJobResult result = glue.getJob(request);

        System.out.println(result.getJob().getName());
        System.out.println(result.getJob().getCommand().getName());
        System.out.println(result.getJob().getCommand().getScriptLocation());
        System.out.println(result.getJob().getCommand().getPythonVersion());
        //////////////////////////////////////////////////
    }

}
```

## Response 정리

* InternalServiceException (500)
* InvalidInputException (400)

```
{
   "Job": { 
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
}
```
