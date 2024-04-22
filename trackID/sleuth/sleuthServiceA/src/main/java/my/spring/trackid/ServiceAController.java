package my.spring.trackid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@Slf4j
public class ServiceAController {

    private final ServiceBClient serviceBClient;

    public ServiceAController(ServiceBClient serviceBClient) {
        this.serviceBClient = serviceBClient;
    }

    @GetMapping(value="/serviceA", produces = MediaType.TEXT_PLAIN_VALUE)
    public String serviceA() {
        log.debug("serviceA is visited...");
        serviceBClient.serviceB();
        return "visited @ " + LocalTime.now();
    }
}
