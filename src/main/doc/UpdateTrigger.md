# DeleteJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_DeleteJob.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.delete_job

### Request 정리

* TriggerName 필수 (1~255 길이)

  * 본인의 trigger만 수정 할 수 있습니다.
  *  trigger의 상태가 RUNNING이 아닌 경우 만 수정 할 수 있습니다.
```json
{
  "TriggerName": "string"
}
```
