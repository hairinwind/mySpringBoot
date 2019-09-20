# perfomrance monitor and lombok

## lombok
this project add lombok, see Person
- @Data can generate getter, setter, equals, hashCode, toString  
- @Builder allows us to create instance using builder pattern. See the junit test class. 

## performance monitor
https://www.baeldung.com/spring-performance-logging  
Add AopConfiguration  
Put in pointcut  
Add logback.xml
```$xslt
<logger name="my.springboot.performancemonitor.service.ServiceDelegate" level="TRACE" />
<logger name="my.springboot.performancemonitor" level="TRACE" />
```
Then start the spring boot 
Visiting http://localhost:8080/person produces the following output
```$xslt
21:32:38.201 [http-nio-8080-exec-1] TRACE m.s.p.service.PersonService - StopWatch 'my.springboot.performancemonitor.service.PersonService.getPerson': running time (millis) = 4
21:32:38.202 [http-nio-8080-exec-1] TRACE m.s.p.service.ServiceDelegate - StopWatch 'my.springboot.performancemonitor.service.ServiceDelegate.getPerson': running time (millis) = 8
21:32:38.202 [http-nio-8080-exec-1] TRACE m.s.p.controller.PersonController - StopWatch 'my.springboot.performancemonitor.controller.PersonController.getPerson': running time (millis) = 13
```

## Spring AOP pointcut expression
https://stackoverflow.com/questions/49884801/wildcard-support-on-package-name-in-spring-aop-pointcut-expression