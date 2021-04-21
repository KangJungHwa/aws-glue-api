# CreateJob

## API Reference

* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.create_job
* https://docs.aws.amazon.com/glue/latest/webapi/API_CreateJob.html

### Request 정리

```
{
   "AllocatedCapacity": number,
   "Command": { 
      "Name": "string",
      "PythonVersion": "string",
      "ScriptLocation": "string"
   },
   "Connections": { 
      "Connections": [ "string" ]
   },
   "DefaultArguments": { 
      "string" : "string" 
   },
   "Description": "string",
   "ExecutionProperty": { 
      "MaxConcurrentRuns": number
   },
   "GlueVersion": "string",
   "LogUri": "string",
   "MaxCapacity": number,
   "MaxRetries": number,
   "Name": "string",
   "NonOverridableArguments": { 
      "string" : "string" 
   },
   "NotificationProperty": { 
      "NotifyDelayAfter": number
   },
   "NumberOfWorkers": number,
   "Role": "string",
   "SecurityConfiguration": "string",
   "Tags": { 
      "string" : "string" 
   },
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

* 에러 발생시 에러 코드(HTTP Status)를 IAM Reference를 확인하여 제대로 리턴하도록 한다.
* 에러가 발생하여 데이터를 제공할 수 없는 경우 XML의 ROOT Element는 생성하도록 한다.

```
{
    'JobRunId': 'string'
}
```
