package my.spring.trackid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ServiceBController {

    @GetMapping("/serviceB")
    public String serviceB() {
        log.info("ServiceB is visited...");
        return "serviceB is visited...";
    }

    @PostMapping("/serviceBPost")
    public String serviceBPost() {
        log.info("ServiceB is visited by Post...");
        return "serviceB is visited by Post...";
    }

}
