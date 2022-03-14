
## command to run it 
```
mvn clean gatling:test -Dgatling.simulationClass=LoadSimulation -Dbase.url=http://localhost:8080/ -Dtest.path=hello/100 -Dsim.users=300
```

```
mvn clean gatling:test -Dgatling.simulationClass=LoadSimulation -Dbase.url=http://localhost:8080/ -Dtest.path=hello/reactive/100 -Dsim.users=300
```

## visualvm
用 visualvm 看并发线程数
- non-reactive 线程 Live 18 Daemon 16, Used Heap is about  
- reactive 线程 Live 18 Daemon 16, Used Heap is about 37M.

## understand the report 
结果中 response time 99th percentile 13729ms 是指 99%的请求的响应时间在 13729ms 以内， 另外 1% 会大于13729ms