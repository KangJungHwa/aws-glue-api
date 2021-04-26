# GetJobs

Retrieves all current job definitions.

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_GetJobs.html

### Request 정리

```json
{
  "MaxResults": number,
  "NextToken": "string"
}
```

## Response 정리

* InternalServiceException (500)
* EntityNotFoundException (400)
* InvalidInputException (400)

```
{
   "Jobs": [ 
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
         "CreatedOn": number,
         "DefaultArguments": { 
            "string" : "string" 
         },
         "Description": "string",
         "ExecutionProperty": { 
            "MaxConcurrentRuns": number
         },
         "GlueVersion": "string",
         "LastModifiedOn": number,
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
         "Timeout": number,
         "WorkerType": "string"
      }
   ],
   "NextToken": "string"
}
```
