package my.springboot.mywebclient;

import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class WebController {

    public static final String HTTPBIN_ORG = "http://www.httpbin.org";

    @GetMapping("/get")
    public Flux<Map> get() {
        log.info("...start running...");

        WebClient webClient = WebClient
                .builder()
                .baseUrl("HTTPBIN_ORG")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Flux<Map> response = webClient.get().uri("/get").retrieve().bodyToFlux(Map.class);
        response.subscribe(result -> {
            log.info("reponse type: {}", result.getClass().getName());
            log.info("response: {}", result);
        });

        return response;
    }

    @GetMapping("/getWithParams")
    public Flux<Map> getWithParams(@RequestParam("param1") String param1) {
        WebClient webClient = WebClient.create(HTTPBIN_ORG);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/get").queryParam("param1", param1).build())
                .retrieve()
                .bodyToFlux(Map.class);
    }

    @GetMapping("/postBody")
    public Mono<Map> postBody() {
        WebClient webClient = WebClient.create("HTTPBIN_ORG");
        return webClient.post()
                .uri("/post")
                .body(BodyInserters.fromObject("CONTENT FROM AN OBJECT..."))
//                .body(Mono.just("THIS IS TEST..."), String.class)
                //Here if use Flux instead of Mono, it could be unbounded stream?
                .retrieve()
                .bodyToMono(Map.class);
    }

    @GetMapping("/filter")
    public Mono<Map> filter() {
        String username = "User1";
        String token = "User1_token";
        WebClient webClient = WebClient.builder().baseUrl(HTTPBIN_ORG)
                .baseUrl(HTTPBIN_ORG)
                .filter(ExchangeFilterFunctions.basicAuthentication(username, token))
                .filter(logRequest())
                .filter(logResposneStatus())
                .build();
        return webClient.get().uri("/get").retrieve().bodyToMono(Map.class);
    }

//    private ExchangeFilterFunction logRequest() {
//        return (clientRequest, next) -> {
//            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
//            clientRequest.headers()
//                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
//            return next.exchange(clientRequest);
//        };
//    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResposneStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    @GetMapping("/getError")
    public Mono<Map> getError() {
        WebClient webClient = WebClient.create(HTTPBIN_ORG);
        return webClient.get().uri("/status/404")
                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
//                        Mono.error(new MyCustomClientException("error: " + clientResponse.statusCode()))
//                )
//                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
//                        Mono.error(new MyCustomClientException("error: " + clientResponse.statusCode()))
//                )
                .bodyToMono(Map.class);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
