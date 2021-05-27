# ListJobs

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_ListJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.list_jobs

### Request 정리
* STANDBY, STOP 상태인 TRIGGER를 실행 할 수 있다.
```json
{


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
