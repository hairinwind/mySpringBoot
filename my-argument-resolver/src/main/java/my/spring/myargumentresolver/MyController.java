package my.spring.myargumentresolver;

import my.spring.myargumentresolver.resolver.HeadOrPathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/hello/{userId}")
    public String hello(@HeadOrPathVariable String userId) {
        return "hello " + userId;
    }
}
