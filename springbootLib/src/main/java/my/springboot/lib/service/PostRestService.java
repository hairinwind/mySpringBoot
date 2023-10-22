package my.springboot.lib.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.springboot.lib.feign.JSONPlaceHolderClient;
import my.springboot.lib.feign.Post;

@Service
public class RestService {

	@Autowired 
	private JSONPlaceHolderClient jsonPlaceHolderClient;
	
	public List<Post> getPosts() {
		return jsonPlaceHolderClient.getPosts();
	}
}
