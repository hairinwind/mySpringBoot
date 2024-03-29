package my.spring.restclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@RestController
public class RestClientController {

    private static final String url = "http://localhost:8080/hello/100"; // the slow service in project my-reactive
    private final WebClient.Builder webClientBuilder;

    public RestClientController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/webclient")
    public Mono<String> webclient() throws ExecutionException, InterruptedException {
        Mono<String> result = webClientBuilder
                .baseUrl(url)
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> e.printStackTrace())
                ;
        return result;
    }

    @GetMapping("/resttemplate")
    public String restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }
}
