package my.springboot.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "jsonPlaceHolderClient", url = "https://jsonplaceholder.typicode.com/", configuration=JSONPlaceHolderClientConfig.class)
public interface JSONPlaceHolderClient {
 
    @RequestMapping(method = RequestMethod.GET, value = "/posts")
    List<Post> getPosts();
 
    @RequestMapping(method = RequestMethod.POST, value = "/posts", produces = "application/json")
    Map<String, Object> postPosts(List<Post> posts);
}
