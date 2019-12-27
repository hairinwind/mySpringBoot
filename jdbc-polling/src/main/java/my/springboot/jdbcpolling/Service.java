package my.springboot.jdbcpolling;

import my.springboot.jdbcpolling.domain.Test;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Service {

    @ServiceActivator(inputChannel = "jdbcPolling-inbound-channel")
    public void handleTestData(List<Test> testList) {
//        List<Test> testList = (List<Test>) obj;
        testList.forEach(test -> {
            System.out.println(test);
            // process the data here ...
        });
    }
}
