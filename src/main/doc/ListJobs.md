# ListJobs

Retrieves the names of all job resources in this AWS account, or the resources with the specified tag. This operation allows you to see which resources are available in your account, and their names.

This operation takes the optional Tags field, which you can use as a filter on the response so that tagged resources can be retrieved as a group. If you choose to use tags filtering, only resources with the tag are retrieved.

## API Reference

* https://docs.aws.amazon.com/glue/latest/webapi/API_ListJobs.html
* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.delete_job

### Request 정리

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
