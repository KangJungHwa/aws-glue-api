# DeleteJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_DeleteJob.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.delete_job

### Request 정리

* JobName 필수 (1~255 길이)
  * JobName 필수로 제공 해야 합니다.
  * JobRun 테이블에 Running 중인 job은 삭제 할 수 없습니다.
  * Trigger에서 사용 중인 Job은 삭제 할 수 없습니다.
  * 본인의 job만 삭제 할 수 있음
```json
{
  "JobName": "string"
}
```

Python 호출코드입니다.

```python
response = client.delete_job(
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
import com.amazonaws.services.glue.model.DeleteJobRequest;
import com.amazonaws.services.glue.model.DeleteJobResult;
import com.amazonaws.services.identitymanagement.model.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeleteJobRequestTester {

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


        DeleteJobRequest request = new DeleteJobRequest();
        request.setJobName("ExampleJob");

        DeleteJobResult result = glue.deleteJob(request);

        System.out.println(result.getJobName());
    }

}
```

## Response 정리

* InternalServiceException (500)
* InvalidInputException (400)

```
{
   "JobName": "string"
}
```
