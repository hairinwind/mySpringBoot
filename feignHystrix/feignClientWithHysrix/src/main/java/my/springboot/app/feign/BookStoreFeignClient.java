package my.springboot.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="BookStoreFeignClient", url="http://localhost:8888") //
public interface BookStoreFeignClient {

    @GetMapping("/recommended")
    String recommend();

    @GetMapping("/wrong_end_point")
    String wrongEndPoint();

}
