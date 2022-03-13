package my.reactive.myreactive;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@CrossOrigin
public class HelloController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~");
    }

    @CrossOrigin
    @GetMapping(path = "/quotes/feed", produces = TEXT_EVENT_STREAM_VALUE )
    public Flux<Quote> getQuotes() {
        return Flux.fromArray(new Quote[] {
           new Quote("DELL", 18.08d),
           new Quote("GOOG", 1234.0d),
           new Quote("AAPL", 1587.0d)
        });
    }

}
