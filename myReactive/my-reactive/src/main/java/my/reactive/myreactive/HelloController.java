package my.reactive.myreactive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Compare the performance between reactive and normal
 */
@RestController
public class HelloController {

    @GetMapping("/hello/{latency}")
    public String hello(@PathVariable long latency) {
        try {
            TimeUnit.MILLISECONDS.sleep(latency);   // 1
        } catch (InterruptedException e) {
            return "Error during thread sleep";
        }
        return "Welcome to non-reactive";
    }

    @GetMapping("/hello/reactive/{latency}")
    public Mono<String> hello(@PathVariable int latency) {
        return Mono.just("Welcome to reactive world ~")
                .delayElement(Duration.ofMillis(latency)); // 1
    }

}
