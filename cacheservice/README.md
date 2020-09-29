https://www.baeldung.com/spring-cache-tutorial

https://spring.io/guides/gs/caching/

## enable cache
```angular2
@EnableCaching
...
@Cacheable("data")
```

## update cache
It gets the data from the service and update the cache by the name(key)
```angular2
@CachePut(value="data")
public int fetchNonCacheData(String name) {
    return fetchDataExpensiveService.fetchData(name);
}
```

## evict cache
```angular2
@CacheEvict(value="data", allEntries=true)
```

## unit test is provided
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
would start a new spring context before each test method

mockito mock service returns different value 
use "thenAnswer"  https://stackoverflow.com/questions/8088179/using-mockito-with-multiple-calls-to-the-same-method-with-the-same-arguments  
https://www.toptal.com/java/a-guide-to-everyday-mockito

spring mvc unit test reference: http://zetcode.com/spring/mockmvc/

## actuator configurations
In application.properties, there are configurations of actuator.
https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html


