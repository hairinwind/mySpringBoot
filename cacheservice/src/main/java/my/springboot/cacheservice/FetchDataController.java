package my.springboot.cacheservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchDataController {

    @Autowired
    private FetchDataService fetchDataService;

    @GetMapping("/fetchdata")
    public int fetchData(@RequestParam(value="name", defaultValue="test") String name) {
        return fetchDataService.fetchData(name);
    }

    @GetMapping("/clearCache") //use get just for easy testing
    @CacheEvict(value="data", allEntries=true)
    public String clearCache() {
        return "cache 'data' is cleared...";
    }

    @GetMapping("/clearCache")
    public String clearCacheProgramatically(){
        Cachemanager
    }

    @GetMapping("/fetchNonCacheData")
    public int fetchNonCacheData(@RequestParam(value="name", defaultValue="test") String name) {
        return fetchDataService.fetchNonCacheData(name);
    }
}