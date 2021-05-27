# ListJobs

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_ListJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.list_jobs

### Request 정리
* 본인의 trigger만 조회할 수 있습니다.
* DependentJob 파라메터를 통해 job이 포함된 Trigger를 조회 할 수 있다.
```json
{
  "MaxResults": number,
  "NextToken": "string",
  "Tags": {
    "string" : "string"
  }
}
```


Python 호출코드입니다.

```python
response = client.list_jobs(
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
import com.amazonaws.services.glue.model.ListJobsRequest;
import com.amazonaws.services.glue.model.ListJobsResult;

public class ListJobsRequestTester {

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
        ListJobsRequest request = new ListJobsRequest();
        request.setMaxResults(2);
        ListJobsResult result = glue.listJobs(request);
        System.out.println(result);
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
   "JobNames": [ "string" ],
   "NextToken": "string"
}
```
