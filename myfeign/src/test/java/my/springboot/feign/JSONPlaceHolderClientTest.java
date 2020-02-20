package my.springboot.feign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JSONPlaceHolderClientTest {
	
	@Autowired
	JSONPlaceHolderClient client;

	@Test
	void testGetPosts() {
		List<Post> posts = client.getPosts();
		System.out.println(posts.size());
		assertNotNull(posts);
	}

	@Test
	void testPostPosts() {
		Post post = new Post(1, 2, "title1", "body1");
		Map<String, Object> result = client.postPosts(List.of(post));
		System.out.println(result.get("0"));
		System.out.println(result.get("0").getClass().getName());
		Map<String, String> postMap = (Map<String, String>)result.get("0");
		assertEquals("title1_new", postMap.get("title"));
	}

}
