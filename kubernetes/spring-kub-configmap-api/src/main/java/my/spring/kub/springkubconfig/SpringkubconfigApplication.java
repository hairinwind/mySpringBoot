package my.spring.kub.springkubconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(DummyConfig.class)
@RefreshScope
public class SpringkubconfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringkubconfigApplication.class, args);
	}

	@Autowired
	private DummyConfig dummyConfig;

	@Value("${config.msg:'default config.msg'}")
	public void setMsg(String message) {
		System.out.println("set message: " + message);
		this.msg = message;
	}

	private String msg;

	@GetMapping("/health")
	public String health() {
		return "success";
	}

	@GetMapping("/message")
	public String hello() {
		return "v1 " +  dummyConfig.getMessage() + " | " + msg + " "
				+ " ["
				+ new SimpleDateFormat().format(new Date())
				+ "]";
	}

}
