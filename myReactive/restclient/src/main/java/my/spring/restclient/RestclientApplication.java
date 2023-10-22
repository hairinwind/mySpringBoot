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
				.maxConnections(180) // 最大并发数
				.pendingAcquireMaxCount(2000) // queue的最大等待数
				.pendingAcquireTimeout(Duration.ofSeconds(45)) // queue 最大等待时间，default
				.build();
		HttpClient httpClient = HttpClient.create(fixedConnectionProvider);
		ReactorClientHttpConnector rcConnector = new ReactorClientHttpConnector(httpClient);
		WebClient.Builder webClientBuilder = WebClient.builder().clientConnector(rcConnector);
		return webClientBuilder;
	}
}
