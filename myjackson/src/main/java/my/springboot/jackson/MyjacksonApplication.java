package my.springboot.jackson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyjacksonApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyjacksonApplication.class, args);
	}
	
	@Bean
    @ConfigurationProperties(prefix = "mail")
    public MailProperties mailProperties() {
        return new MailProperties();
    }

}
