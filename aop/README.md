# Retry

https://www.baeldung.com/spring-retry

## retryable
Visiting http://localhost:8080/dataRetry shall display "abc".
Check the log, you shall see
```$xslt
DEBUG 23388 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Retry: count=0
 INFO 23388 --- [nio-8080-exec-1] m.s.aop.service.RetryDelegateService     : ... execute time 1
DEBUG 23388 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Checking for rethrow: count=1
DEBUG 23388 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Retry: count=1
 INFO 23388 --- [nio-8080-exec-1] m.s.aop.service.RetryDelegateService     : ... execute time 2
 INFO 23388 --- [nio-8080-exec-1] m.s.aop.service.RetryDelegateService     : ... retried 2 times, return correct data
```

## recover 
Visiting http://localhost:8080/dataRecovery shall display "this is recover data".
Check the log, you shall see 
```$xslt
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Retry: count=0
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Checking for rethrow: count=1
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Retry: count=1
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Checking for rethrow: count=2
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Retry: count=2
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Checking for rethrow: count=3
DEBUG 12508 --- [nio-8080-exec-1] o.s.retry.support.RetryTemplate          : Retry failed last attempt: count=3
 INFO 12508 --- [nio-8080-exec-1] m.s.aop.service.RetryDelegateService     : ...getDataRecovery() is executed
```

## RetryTemplate
Visiting http://localhost:8080/dataByRetryTemplate shall display "abc".
Check the log, you will see 
```$xslt
2019-09-30 17:22:50.868  INFO 24724 --- [nio-8080-exec-1] m.s.aop.service.RetryDelegateService     : ... execute time 1
...
2019-09-30 17:22:53.870  INFO 24724 --- [nio-8080-exec-1] m.s.aop.service.RetryDelegateService     : ... execute time 2
```
the interval is 3 seconds.

## exponentialBackOffRetryTemplate
Visiting http://localhost:8080/dataByExponentialTemplate shall display "abc".
Check the log, you will see the sleeping time is doubled, like 2000, 4000...
```$xslt
o.s.retry.support.RetryTemplate          : Retry: count=0
o.s.r.backoff.ExponentialBackOffPolicy   : Sleeping for 2000
o.s.retry.support.RetryTemplate          : Checking for rethrow: count=1
o.s.retry.support.RetryTemplate          : Retry: count=1
o.s.r.backoff.ExponentialBackOffPolicy   : Sleeping for 4000
o.s.retry.support.RetryTemplate          : Retry: count=2
m.s.aop.service.RetryDelegateService     : ... retried 3 times, return correct data
```
More sample code regarding exponentialBackOffRetryTemplate could be found https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.retry.backoff.ExponentialBackOffPolicy

# Pointcut
https://www.baeldung.com/spring-aop-pointcut-tutorial


# AOP
https://www.journaldev.com/2583/spring-aop-example-tutorial-aspect-advice-pointcut-joinpoint-annotations  

https://www.javainuse.com/spring/spring-boot-aop


