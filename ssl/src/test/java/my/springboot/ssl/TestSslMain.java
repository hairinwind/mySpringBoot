package my.springboot.ssl;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * This is the code to test SSL with JRE cacerts imported with certificate
 */
public class TestSslMain {

    public static void main(String[] args) throws Exception {
        String response = restTemplate().getForObject("https://localhost:8443/greeting?name=test", String.class);
        System.out.println("response: " + response);
    }

    static RestTemplate restTemplate() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

}
