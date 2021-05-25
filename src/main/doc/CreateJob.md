# CreateJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_CreateJob.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.create_job

### Request 정리

* Name은 필수 (1~255 길이)
* aws job 에는 스크립트 종류를 구별할 방법이 있어 명명 규칙이 필요함. 
* 명명규칙 
  * Python : PY_
  * R : R_
  * MATRAP : MAT_ 
  
```json
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
   "DefaultArguments": { 
      "string" : "string" 
   },
   "Description": "string",
   "ExecutionProperty": { 
      "MaxConcurrentRuns": number
   },
   "GlueVersion": "string",
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
   "Tags": { 
      "string" : "string" 
   },
   "Timeout": number,
   "WorkerType": "string"
}
```

Python 호출코드입니다.

```python
response = client.start_job_run(
    JobName='string',
    JobRunId='string',
    Arguments={
        'string': 'string'
    },
    AllocatedCapacity=123,
    Timeout=123,
    MaxCapacity=123.0,
    SecurityConfiguration='string',
    NotificationProperty={
        'NotifyDelayAfter': 123
    },
    WorkerType='Standard'|'G.1X'|'G.2X',
    NumberOfWorkers=123
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
import com.amazonaws.services.glue.model.CreateJobRequest;
import com.amazonaws.services.glue.model.CreateJobResult;
import com.amazonaws.services.glue.model.JobCommand;
import com.amazonaws.services.identitymanagement.model.Tag;

import java.util.ArrayListac;
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
