package my.reactive.myreactive;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiFunction;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@CrossOrigin
public class QuoteController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~");
    }

    @CrossOrigin
    @GetMapping(path = "/quotes/feed", produces = TEXT_EVENT_STREAM_VALUE )
    public Flux<Quote> getQuotes() {
        Quote[] array = {
                new Quote("DELL", 18.08d),
                new Quote("GOOG", 1234.0d),
                new Quote("AAPL", 1587.0d)
        };
//        Flux<Quote> quoteFlux = Flux.fromArray(array);
//        array[0] = new Quote("MSFT", 111.00d);

//        Flux<Quote> quoteFlux = Flux.just(new Quote("DELL", 18.08d), new Quote("GOOG", 1234.0d));

        Flux<Quote> quoteFlux = Flux.generate(
                () -> 0,
                (BiFunction<Integer, SynchronousSink<Quote>, Integer>)(state, sink) -> {
                    sink.next(array[state]);
                    state++;
                    if (state > array.length - 1) {
                        sink.complete();
                    }
                    return state;
                },
                // finally 消费状态
                state -> System.out.println("state = " + state)
        ).doFinally((signalType) -> {
                    System.out.println("quoteFlux is " + signalType);
        }).take(2).log();

        return quoteFlux;
    }

}
