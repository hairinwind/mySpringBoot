package io.springoneplatform.demo.tradingservicewebflux;

import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.time.Duration.ofMillis;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
public class QuotesController {

    @CrossOrigin
    @GetMapping("/quotes")
    @ResponseBody
    public Mono<String> quotes() {
        return Mono.just("quotes " + LocalDateTime.now());
    }

    @CrossOrigin
    @GetMapping(path = "/quotes/feed", produces = TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Quote> quotesStream() {
        return WebClient.create("http://localhost:8081")
                        .get()
                        .uri("/quotes")
                        .accept(APPLICATION_STREAM_JSON)
                        .retrieve()
                        .bodyToFlux(Quote.class)
                        .delayElements(ofMillis(600))
                        .log("io.springoneplatform.demo.tradingservicewebflux");
    }
}