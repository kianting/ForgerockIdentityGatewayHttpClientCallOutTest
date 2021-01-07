# ForgerockIdentityGatewayHttpClientCallOutTest

This repository is here to test the Promise Framwork issue tested on the forgerock ticket.

- https://backstage.forgerock.com/support/tickets?id=56495

The test script iss at `Test.groovy` and test route is `00_test_route.json`.

Use the following request to test

```
 curl -v --location --request GET 'http://localhost:9081/todos/1'
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 9081 (#0)
> GET /todos/1 HTTP/1.1
> Host: localhost:9081
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 401 Unauthorized
< Content-Length: 70
< Content-Type: application/json; charset=UTF-8
<
* Connection #0 to host localhost left intact
{"code":401,"reason":"Unauthorized","message":"Authentication Failed"}* Closing connection 0
```

Which will generate the following logs in IG, hence showing that in IG the error wont be swallowed up, but users have to put a third function to catch runtime errors. Otherwise the unknowhost error wont bubble up.

```
[vert.x-eventloop-thread-6] INFO  o.f.o.d.c.C.capture.TestHTTPCallout @00_test_route -

--- (request) id:83ce641a-3815-448f-aa6e-74bdf4b2e780-776 --->

GET https://jsonplaceholder.typicode.com/todos/1 HTTP/1.1
Accept: */*
Host: localhost:9081
User-Agent: curl/7.64.1

[vert.x-eventloop-thread-6] INFO  o.f.o.f.S.TestHTTPCallout @00_test_route - Starting Test HTTP Call out from IG
[vert.x-eventloop-thread-6] INFO  o.f.o.f.S.TestHTTPCallout @00_test_route - Getting Payload from test endpoint endpoint: https://httpbin.orgnn/get
[vert.x-eventloop-thread-6] INFO  o.f.o.f.S.TestHTTPCallout @00_test_route - >>>>>>A Runtime exception Occured !!! java.net.UnknownHostException: failed to resolve 'httpbin.orgnn' after 2 queries
[vert.x-eventloop-thread-6] INFO  o.f.o.f.S.TestHTTPCallout @00_test_route - Returning Unauthorized error
[vert.x-eventloop-thread-6] INFO  o.f.o.d.c.C.capture.TestHTTPCallout @00_test_route -

<--- (filtered-response) id:83ce641a-3815-448f-aa6e-74bdf4b2e780-776 ---

HTTP/1.1 401 Unauthorized
Content-Length: 70
Content-Type: application/json; charset=UTF-8

[entity]
```