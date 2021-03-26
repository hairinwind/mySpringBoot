package my.springboot.app;

import my.springboot.app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@RestController
public class FeignClientWithHysrixApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(FeignClientWithHysrixApplication.class, args);
    }

    @RequestMapping("/to-read")
    public String toRead() {
        return bookService.readingList();
    }

    @RequestMapping("/to-read1")
    public String toRead1() {
        return bookService.readingList1();
    }
}
