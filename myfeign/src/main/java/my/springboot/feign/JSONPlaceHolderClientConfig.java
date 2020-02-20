package my.springboot.feign;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

/**
 * Spring Cloud creates a new default set on demand for each named client using the FeignClientsConfiguration class 
 * that we can customize as explained in the next section.
 * 
 * Decoder – ResponseEntityDecoder, which wraps SpringDecoder, used to decode the Response
 * Encoder – SpringEncoder, used to encode the RequestBody
 * Logger – Slf4jLogger is the default logger used by Feign
 * Contract – SpringMvcContract, which provides annotation processing
 * Feign-Builder – HystrixFeign.Builder used to construct the components
 * Client – LoadBalancerFeignClient or default Feign client
 * 
 * https://www.baeldung.com/spring-cloud-openfeign
 */
@Configuration
public class JSONPlaceHolderClientConfig {
	
	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;

	@Bean
	public SpringEncoder encode() {
		return new MySpringEncoder(messageConverters);
	}
	
	@Bean
	// either declare a Bean or put in application.yml 
	// feign.client.config.default.requestInterceptors: com.baeldung.cloud.openfeign.JSONPlaceHolderInterceptor
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			requestTemplate.header("user", "my_test");
		};
	} 
}
