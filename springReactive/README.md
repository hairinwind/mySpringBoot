# spring reactive sample project 

copied from https://github.com/violetagg/s1p2017-demo.git

My Notes: https://www.notion.so/Spring-Reactive-3d197364cc4f44819a2dce41e2df4ed8

# trading-service-webflux
provides endpoint "http://localhost:8080/quotes/feed"

and it will send GET request to "http://localhost:8081/quotes"

# stock-quotes-webflux
provides endpoint "http://localhost:8081/quotes", which generates quotes  

# load-generator 
mimic concurrent clients, send request to trading-service-webflux
