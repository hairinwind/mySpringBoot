package my.spring.kubernetes.springkub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringkubApplication {

	@Value("${config.msg}")
	public String message;

	public static void main(String[] args) {
		SpringApplication.run(SpringkubApplication.class, args);
	}

	@GetMapping("/message")
	public String getMessage() {
		System.out.println("endpoint /message is called...");
		return message;
	}

}
