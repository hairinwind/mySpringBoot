package my.springboot.aop.service;

import org.springframework.stereotype.Service;

@Service
public class RetryService {

    public String getDataRetry() {
        return "abc";
    }
}
