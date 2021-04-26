# GetJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_ListJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.delete_job

### Request 정리

* JobName 필수 (1~255 길이)

```json
{
  "MaxResults": number,
  "NextToken": "string",
  "Tags": {
    "string" : "string"
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
