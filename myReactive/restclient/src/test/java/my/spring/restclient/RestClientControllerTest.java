package my.spring.restclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(RestClientController.class)
class RestClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void webclient() {
        webTestClient.get()
                .uri("/webclient")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Welcome to non-reactive")
        ;
    }
}