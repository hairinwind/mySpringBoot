package my.springboot.cacheservice;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FetchDataExpensiveService {

    AtomicInteger count = new AtomicInteger(0);

    public int fetchData(String param1) {
        System.out.println("...This is the expensive service to run...");
        return count.addAndGet(1);
    }
}
