# CreateJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_CreateJob.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.create_job

### Request 정리
* Name은 필수 (1~255 길이)
  * JobName은 Unique 해야 합니다.
* Job 생성 시 사용자 Z drive를 공용 Storage로 copy 합니다.
* Job의 Type(PYTHON,R, MATLAB) 구분은 Command tag의 Name을 가지고 구분합니다.
  * job 생성시  Command의 Name에 PYTHON,R, MATLAB 외의 값을 입력하면 400 에러를 반환합니다. 
* scriptLocation은 드라이브 식별자는 빼고 이하 경로로만 입력합니다.
  * 아래와 같이 Z 드라이브 ROOT에 script가 있는 경우는 파일명만 입력합니다.
  * ex) Z:/sample.py =>  sample.py

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

        //////////////////////////////////////////////////
        CreateJobRequest request = new CreateJobRequest();
        JobCommand command = new JobCommand();
        //command.setName("print_number.py");
        command.setName("PYTHON");
        command.setPythonVersion("3.7.1");
        command.setScriptLocation("print_alphpbet.py");
        request.setCommand(command);
        request.setName("PY_PRINT1");

        CreateJobResult result = glue.createJob(request);

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
