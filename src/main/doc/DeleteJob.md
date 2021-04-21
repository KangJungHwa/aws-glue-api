# CreateJob

## API Reference

* https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/glue.html#Glue.Client.create_job

### Request 정리

* Path는 지정할 수없음. 따라서 타 API에서도 사용자 식별에 Path는 최소화해야함.

```
https://iam.amazonaws.com/?Action=GetUser
&UserName=Bob
&Version=2010-05-08
&AUTHPARAMS
```

## Response 정리

* 에러 발생시 에러 코드(HTTP Status)를 IAM Reference를 확인하여 제대로 리턴하도록 한다.
* 에러가 발생하여 데이터를 제공할 수 없는 경우 XML의 ROOT Element는 생성하도록 한다.

```xml
<GetUserResponse xmlns="https://iam.amazonaws.com/doc/2010-05-08/">
    <GetUserResult>
        <User>
            <UserId>AIDACKCEVSQ6C2EXAMPLE</UserId>
            <Path>/division_abc/subdivision_xyz/</Path>
            <UserName>Bob</UserName>
            <Arn>arn:aws:iam::123456789012:user/division_abc/subdivision_xyz/Bob</Arn>
            <CreateDate>2013-10-02T17:01:44Z</CreateDate>
            <PasswordLastUsed>2014-10-10T14:37:51Z</PasswordLastUsed>
        </User>
    </GetUserResult>
    <ResponseMetadata>
        <RequestId>7a62c49f-347e-4fc4-9331-6e8eEXAMPLE</RequestId>
    </ResponseMetadata>
</GetUserResponse>
```
