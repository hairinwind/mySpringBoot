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
