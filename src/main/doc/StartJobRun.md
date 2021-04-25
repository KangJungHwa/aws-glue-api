# CreateJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_StartJobRun.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.create_job

### Request 정리

* JobName (필수)
* JobRunId 재실행을 위한 ID (옵션)
* Arguments 인자 (옵션)

```javascript
{
   "AllocatedCapacity": number,
   "Arguments": { 
      "string" : "string" 
   },
   "JobName": "string",
   "JobRunId": "string",
   "MaxCapacity": number,
   "NotificationProperty": { 
      "NotifyDelayAfter": number
   },
   "NumberOfWorkers": number,
   "SecurityConfiguration": "string",
   "Timeout": number,
   "WorkerType": "string"
}
```

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

## Response 정리

* EntityNotFoundException (400)
* InvalidInputException (400)
* InternalServiceException (500)

```javascript
{
    "JobRunId": "string"
}
```
