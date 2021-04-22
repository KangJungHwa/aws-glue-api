# CreateJob

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_DeleteJob.html

### Request 정리

* Job이 Trigger 되어 있으면 어떻게 해야하나?

```
{
   "JobName": "string"
}
```

## Response 정리

* Job Name이 비어있으면 400
* Job이 없으면 200

```
{
   "JobName": "string"
}
```