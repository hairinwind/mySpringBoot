package my.spring.restclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@SpringBootApplication
public class RestclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestclientApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		ConnectionProvider fixedConnectionProvider = ConnectionProvider.builder("fixed")
				.maxConnections(180) // not working
				.pendingAcquireTimeout(Duration.ofSeconds(1))
				.build();
		HttpClient httpClient = HttpClient.create(fixedConnectionProvider);
		ReactorClientHttpConnector rcConnector = new ReactorClientHttpConnector(httpClient);
		WebClient.Builder webClientBuilder = WebClient.builder().clientConnector(rcConnector);
		return webClientBuilder;
	}
}
