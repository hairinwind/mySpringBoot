
## the feign log 
it does not output the traceId/spanId in the feignclient log.  
```dbn-psql
[ServiceBClient#serviceBPost] ---> POST http://localhost:8080/serviceBPost HTTP/1.1
[ServiceBClient#serviceBPost] Content-Length: 69
[ServiceBClient#serviceBPost] Content-Type: application/json
[ServiceBClient#serviceBPost] 
[ServiceBClient#serviceBPost] {"metadata-key2":"metadata-value2","metadata-key1":"metadata-value1"}
[ServiceBClient#serviceBPost] ---> END HTTP (69-byte body)
```
but it can be founded in "s.n.www.protocol.http.HttpURLConnection"
```
s.n.www.protocol.http.HttpURLConnection  : sun.net.www.MessageHeader@db1ca18 pairs: {POST /serviceBPost HTTP/1.1: null}{Content-Type: application/json}{traceparent: 00-6626ec465c5de0bd0bc6526ce2ffd904-fd4a189f27247450-00}{Accept: */*}{User-Agent: Java/21.0.1}{Host: localhost:8080}{Connection: keep-alive}{Content-Length: 69}
```

so you need turn on the log on "s.n.www.protocol.http.HttpURLConnection"  
```dbn-psql
logging.level.sun.net.www.protocol.http.HttpURLConnection=DEBUG
```

## to test 
start serviceB  
start serviceA  
then visit http://localhost:8081/serviceA

