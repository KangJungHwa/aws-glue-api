# DeleteJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_UpdateJob.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.update_job

### Request 정리

* JobName 필수 (1~255 길이)

```json
{
  "JobName": "string"
}
```

Python 호출코드입니다.

```python
response = client.update_job(
    JobName='string'
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
        UpdateJobRequest request = new UpdateJobRequest();
        JobUpdate update = new JobUpdate();
        JobCommand command = new JobCommand();

        command.setName("sample5");
        command.setPythonVersion("3.7.1");
        command.setScriptLocation("s3:/test_update.py");
        update.setCommand(command);
        request.setJobName("sample5");
        request.setJobUpdate(update);

        UpdateJobResult result = glue.updateJob(request);

        System.out.println(result.getJobName());


        //////////////////////////////////////////////
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
