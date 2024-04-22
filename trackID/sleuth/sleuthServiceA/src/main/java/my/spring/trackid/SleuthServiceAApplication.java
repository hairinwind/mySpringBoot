package my.spring.trackid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"my.spring.trackid"})
public class SleuthServiceAApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleuthServiceAApplication.class, args);
	}

}
