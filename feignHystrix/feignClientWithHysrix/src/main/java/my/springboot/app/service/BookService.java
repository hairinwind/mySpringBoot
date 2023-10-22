package my.springboot.app.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import my.springboot.app.feign.BookStoreFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    BookStoreFeignClient bookStoreFeignClient;

    public String readingList() {
        return bookStoreFeignClient.recommend();
    }

    @HystrixCommand(fallbackMethod = "reliable")
    public String readingList1() {
        return bookStoreFeignClient.wrongEndPoint();
    }

    public String reliable() {
        return "Cloud Native Java (O'Reilly)";
    }

}
