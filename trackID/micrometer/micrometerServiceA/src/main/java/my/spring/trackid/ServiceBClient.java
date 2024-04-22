package my.spring.trackid;

import feign.HeaderMap;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "ServiceBClient", url = "${serviceB.url}", configuration = FeignConfig.class)
public interface ServiceBClient {

    @GetMapping("/serviceB")
    @Headers("Static-Header: staticValue")
    String serviceB();

    @PostMapping("/serviceBPost")
    String serviceBPost(@HeaderMap Map<String, Object> headers);

}
