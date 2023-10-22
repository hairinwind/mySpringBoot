package io.springoneplatform.demo.tradingservicewebflux;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class QuoteRestController {

    @CrossOrigin
    @GetMapping("/getQuote")
    public String getQuote() {
        return "quote " + LocalDateTime.now();
    }
}
