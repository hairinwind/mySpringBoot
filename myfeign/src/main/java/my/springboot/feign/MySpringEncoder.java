package my.springboot.feign;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import feign.RequestTemplate;
import feign.codec.EncodeException;

public class MySpringEncoder extends SpringEncoder {

	public MySpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
	}
	
	@Override
	public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
		if (requestBody instanceof List) {
			Post post = ((List<Post>)requestBody).get(0);
			post.setTitle(post.getTitle()+ "_new");
		}
		super.encode(requestBody, bodyType, request);
	}

}
