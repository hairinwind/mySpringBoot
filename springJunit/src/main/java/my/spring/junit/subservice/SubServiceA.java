package my.spring.junit.subservice;

import org.springframework.stereotype.Component;

@Component
public class SubServiceA {

    public String toUpperCase(String str) {
        return str != null ? str.toUpperCase() : null;
    }

}
