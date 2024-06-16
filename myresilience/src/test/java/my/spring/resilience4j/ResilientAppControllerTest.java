package my.spring.resilience4j;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 9090)
@Slf4j
class ResilientAppControllerTest {

    @LocalServerPort
    private int port;

    @Value("#{T(java.lang.Integer).parseInt('${resilience4j.circuitbreaker.instances.CircuitBreakerService.wait-duration-in-open-state}'.replace('s', ''))}")
    private int waitDurationInSeconds;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Test
    public void testCircuitBreaker() {
        System.out.println("spring random port is " + port);
        stubFor(WireMock.get("/api/external")
                .willReturn(serverError()));

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("CircuitBreakerService");
        System.out.println(circuitBreaker.getState());
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());

        IntStream.rangeClosed(1, 5)
                .forEach(i -> {
                    ResponseEntity response = testRestTemplate.getForEntity("/api/circuit-breaker", String.class);
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });

        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        IntStream.rangeClosed(1, 5)
                .forEach(i -> {
                    ResponseEntity response = testRestTemplate.getForEntity("/api/circuit-breaker", String.class);
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                });

        verify(5, getRequestedFor(urlEqualTo("/api/external")));

        try {
            Thread.sleep(waitDurationInSeconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        // external service back to normal
        stubFor(WireMock.get(WireMock.urlEqualTo("/api/external"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Hello, World!\"}")));

        IntStream.rangeClosed(1, 3)
                .forEach(i -> {
                    ResponseEntity response = testRestTemplate.getForEntity("/api/circuit-breaker", String.class);
                    assertThat(response.getStatusCode()).isEqualTo(OK);
                });

        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }

    @Test
    public void testRetry() {
        stubFor(WireMock.get("/api/external")
                .willReturn(ok()));
        ResponseEntity<String> response1 = testRestTemplate.getForEntity("/api/retry", String.class);
        verify(1, getRequestedFor(urlEqualTo("/api/external")));

        WireMock.resetAllRequests();

        stubFor(WireMock.get("/api/external")
                .willReturn(serverError()));
        ResponseEntity<String> response2 = testRestTemplate.getForEntity("/api/retry", String.class);
        assertEquals(response2.getBody(), "all retries have exhausted");
        verify(3, getRequestedFor(urlEqualTo("/api/external")));
    }

    @Test
    public void testWireMockServer() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/external"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Hello, World!\"}")));
        ResponseEntity response = testRestTemplate.getForEntity("/api/circuit-breaker", String.class);
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hello, World!"));
    }

    @Test
    public void testTimeLimiter() {
        stubFor(WireMock.get("/api/external").willReturn(ok()));
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/time-limiter", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.REQUEST_TIMEOUT);
        verify(1, getRequestedFor(urlEqualTo("/api/external")));
    }

    @Test
    void testBulkhead() throws Exception {
        stubFor(WireMock.get("/api/external")
                .willReturn(ok()));
        Map<Integer, Integer> responseStatusCount = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        IntStream.rangeClosed(1, 5)
                .forEach(i -> executorService.execute(() -> {
                    ResponseEntity response = testRestTemplate.getForEntity("/api/bulkhead", String.class);
                    int statusCode = response.getStatusCodeValue();
                    responseStatusCount.merge(statusCode, 1, Integer::sum);
                    latch.countDown();
                }));
        latch.await();
        executorService.shutdown();

        assertEquals(2, responseStatusCount.keySet().size());
        log.info("Response statuses: " + responseStatusCount.keySet());
        log.info("Response statuses count: " + responseStatusCount);
        assertTrue(responseStatusCount.containsKey(BANDWIDTH_LIMIT_EXCEEDED.value()));
        assertTrue(responseStatusCount.containsKey(OK.value()));
        verify(3, getRequestedFor(urlEqualTo("/api/external")));
    }

    @Test
    public void testRatelimiter() {
        stubFor(WireMock.get("/api/external")
                .willReturn(ok()));
        Map<Integer, Integer> responseStatusCount = new ConcurrentHashMap<>();

        IntStream.rangeClosed(1, 50)
                .parallel()
                .forEach(i -> {
                    ResponseEntity<String> response = testRestTemplate.getForEntity("/api/rate-limiter", String.class);
                    int statusCode = response.getStatusCodeValue();
                    responseStatusCount.put(statusCode, responseStatusCount.getOrDefault(statusCode, 0) + 1);
                });

        assertEquals(2, responseStatusCount.keySet().size());
        log.info("Response statuses count: {} ", responseStatusCount);
        assertTrue(responseStatusCount.containsKey(TOO_MANY_REQUESTS.value()));
        assertTrue(responseStatusCount.containsKey(OK.value()));
        verify(5, getRequestedFor(urlEqualTo("/api/external")));
    }
}