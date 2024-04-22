package my.spring.trackid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ServiceBClient", url = "${serviceB.url}", configuration = FeignConfig.class)
public interface ServiceBClient {

    @GetMapping("/serviceB")
    String serviceB();

}
