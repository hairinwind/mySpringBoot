package my.springboot.feign.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import my.springboot.feign.DynamicUrlFeignClient;
import my.springboot.feign.JSONPlaceHolderClient;
import my.springboot.feign.Post;

@RestController
public class MyFeignController {
	
	@Autowired
	JSONPlaceHolderClient jsonPlaceHolderClient;
	
	@Autowired
	DynamicUrlFeignClient dynamicUrlFeignClient;

	@GetMapping("/test")
    public ResponseEntity<Object> test() {
		List<Post> posts = jsonPlaceHolderClient.getPosts();
        return ResponseEntity.status(HttpStatus.OK).body(posts); 
    }
	
	@GetMapping("/testDynamicUri")
    public ResponseEntity<Object> testDynamicUri() throws URISyntaxException {
		URI uri = new URI("https://jsonplaceholder.typicode.com/posts");
		List<Post> posts = dynamicUrlFeignClient.getPosts(uri);
        return ResponseEntity.status(HttpStatus.OK).body(posts); 
    }
	
}
