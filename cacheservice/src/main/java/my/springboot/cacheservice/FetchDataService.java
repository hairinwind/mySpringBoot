package my.springboot.cacheservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FetchDataService {

    @Autowired
    private FetchDataExpensiveService fetchDataExpensiveService;

    @Cacheable("data")
    public int fetchData(String name) {
        return fetchDataExpensiveService.fetchData(name);
    }

    /**
     * The CachePut can update the value cached
     * for example if value 1 is cached for the argument name="test"
     * this method is triggered with argument name="test" and the service returns 2
     * the CachePut annotation let spring to update the cached value from 1 to 2
     *
     * To test it, visit http://localhost:8080/fetchdata?name=test
     * value 1 is returned and cached
     * Then visit http://localhost:8080/fetchNonCacheData?name=test
     * value 2 is returned and spring updates the cache value
     * visit http://localhost:8080/fetchData?name=test again, it shall return 2 from cache
     *
     * @param name
     * @return
     */
    @CachePut(value="data")
    public int fetchNonCacheData(String name) {
        return fetchDataExpensiveService.fetchData(name);
    }
}
