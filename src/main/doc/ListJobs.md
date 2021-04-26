# ListJobs

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_ListJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.list_jobs

### Request 정리

* JobName 필수 (1~255 길이)

```json
{
    NextToken='string',
    MaxResults=123,
    Tags={
        'string': 'string'
    }
}
```

Python 호출코드입니다.

```python
response = client.list_jobs(
    NextToken='string',
    MaxResults=123,
    Tags={
        'string': 'string'
    }
)
```

Java 호출 코드입니다.
```
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
        request.setMaxResults(3);
        request.setNextToken("\t");
        ListJobsResult result=glue.listJobs(request);
        System.out.println(result);
        //////////////////////////////////////////////
    }
```