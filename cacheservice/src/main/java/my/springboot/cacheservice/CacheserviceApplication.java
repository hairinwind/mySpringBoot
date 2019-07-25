package my.springboot.cacheservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CacheserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheserviceApplication.class, args);
    }

}
