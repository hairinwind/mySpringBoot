package my.spring.junit.controller;

import my.spring.junit.service.ServiceA;
import my.spring.junit.service.ServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private ServiceA serviceA;

    @Autowired
    private ServiceB serviceB;

    @GetMapping("/test")
    public String info() {
        return "This is test controller...";
    }

}
