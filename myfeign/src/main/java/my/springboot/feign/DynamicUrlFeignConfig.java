package my.springboot.feign;

import org.springframework.context.annotation.Bean;

import feign.Contract;

public class DynamicUrlFeignConfig {

//	MyRequestInterceptor myRequestInterceptor;
//	
//	class MyRequestInterceptor implements RequestInterceptor {
//
//		@Override
//		public void apply(RequestTemplate template) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
//	
//	@PostConstruct
//	public void init() {
//		myRequestInterceptor = new MyRequestInterceptor();
//	}
//
//	@Bean
//	@Scope("prototype")
//	public Feign.Builder feignBuilder() {
//		return Feign.builder().requestInterceptor(myRequestInterceptor);
//	}

	// This is important when you try to call feign with pass in URI parameter
	@Bean
	public Contract feignContract() {
		return new Contract.Default();
	}

}
