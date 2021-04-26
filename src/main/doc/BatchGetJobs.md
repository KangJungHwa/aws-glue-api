# GetJobs

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_BatchGetJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.batch_get_jobs

### Request 정리

```json
{
   "JobNames": [ "string" ]
}
```

Python 호출코드입니다.

```python
response = client.batch_get_jobs(
    JobNames=[
        'string',
    ]
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
         BatchGetJobsRequest request = new BatchGetJobsRequest();
         List<String> jobnames = new ArrayList<>();
         jobnames.add("sample");
         jobnames.add("sample1");
         request.setJobNames(jobnames);
 
 
         BatchGetJobsResult result=glue.batchGetJobs(request);
         System.out.println(result);
         for (int i = 0; i <result.getJobs().size() ; i++) {
             System.out.println(result.getJobs().get(i).getName());
             System.out.println(result.getJobs().get(i).getCommand().getName());
             System.out.println(result.getJobs().get(i).getCommand().getScriptLocation());
             System.out.println(result.getJobs().get(i).getCommand().getPythonVersion());
         }
         //////////////////////////////////////////////
     }

```