We use standard HTTP status codes:
* 200 to 299 if the request succeeded. This includes code 202 if the API needs to wait for further action
* 400 to 499 if the request failed because of a client error by your application
* 500 to 599 if the request failed because of an error on our server

Errors specific to each API are shown in the Endpoints section, under Response. For more on errors see our [HMRC Developer Hub Reference guide](https://developer.service.hmrc.gov.uk/api-documentation/docs/reference-guide#errors).