package my.web.ssl.client;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;


@SpringBootApplication
public class WebMutualSslClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebMutualSslClientApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {
		//use jvm arguments
		RestTemplate restTemplate = new RestTemplate();
		// programmatically using sslContext
//		RestTemplate restTemplate = getRestTemplate();

		String apiUrl = "https://localhost/api/hello";

		// Make the REST API call
		String response = restTemplate.getForObject(apiUrl, String.class);

		// Process the response as needed
		System.out.println("API response: " + response);
	}

	private static RestTemplate getRestTemplate() throws Exception {
		final String storePassword = "changeit";
		final String keyPassword = "password";

		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(ResourceUtils.getFile("classpath:truststore.p12"), storePassword.toCharArray())
				.loadKeyMaterial(ResourceUtils.getFile("classpath:client_keystore.p12"), storePassword.toCharArray(), storePassword.toCharArray())
				.build();

		final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
				.setSslContext(sslContext)
				.build();
		final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(sslSocketFactory)
				.build();
		CloseableHttpClient client = HttpClients.custom()
				.setConnectionManager(cm)
				.evictExpiredConnections()
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}


}
