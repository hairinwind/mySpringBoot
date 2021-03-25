package my.springboot.app.service;

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
}
