package my.springboot.feign;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;

import feign.HeaderMap;
import feign.RequestLine;

@FeignClient(value = "dynamicUrlFeignClient", 
	url="runtimePassedIn", // this cannot not be ignored
	configuration=DynamicUrlFeignConfig.class)
public interface DynamicUrlFeignClient {

	@RequestLine("GET")
    List<Post> getPosts(URI uri, @HeaderMap Map<String, String> header);
}
