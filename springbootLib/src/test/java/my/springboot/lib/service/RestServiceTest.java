package my.springboot.lib.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import my.springboot.lib.feign.Post;

@SpringBootTest(classes=SpringBootLibConfig.class)
@ExtendWith(SpringExtension.class)
class RestServiceTest {
	
	@Autowired
	RestService restService;

	@Test
	void testGetPosts() {
		List<Post> posts = restService.getPosts();
		posts.stream().forEach(System.out::println);
		assertNotNull(posts);
	}

}
