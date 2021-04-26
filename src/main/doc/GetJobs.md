# GetJobs

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_GetJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.get_jobs

### Request 정리

```json
{
    NextToken='string',
    MaxResults=123
}
```

Python 호출코드입니다.

```python
response = client.get_jobs(
    NextToken='string',
    MaxResults=123
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
            GetJobsRequest request = new GetJobsRequest();
            request.setMaxResults(3);
            GetJobsResult result=glue.getJobs(request);
            System.out.println(result);
            for (int i = 0; i <result.getJobs().size() ; i++) {
                System.out.println(result.getJobs().get(i).getName());
                System.out.println(result.getJobs().get(i).getCommand().getName());
                System.out.println(result.getJobs().get(i).getCommand().getScriptLocation());
                System.out.println(result.getJobs().get(i).getCommand().getPythonVersion());
            }
            //////////////////////////////////////////////
```