# Setup
## AWS Access
1. `touch $HOME/.aws/credentials`
1. Add the following content to the `$HOME/.aws/credentials`:
    ```
    [default]
    aws_access_key_id=<your_access_key_id>
    aws_secret_access_key=<your_secret_key>
    ``` 

## Gradle Goals
1. Create fat jar:
    ```
    gradle clean jarFat
    ``` 
1. Upload fat jar to s3 and update lambda function code:
    ```
    gradle deployLambda
    ```
    By default, lambda name is `javaClubEntryPoint`. You may specify lambda name explicitly:
    ```
    gradle -PlambdaName=<other_lambda_name> deployLambda
    ``` 

## High Level Architecture
![Drag Racing](docs/parkingSystemHighLevelArchitecture.jpg)