package my.springboot.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class PropertyEncryptionApplication implements CommandLineRunner {
	
	@Value("${spring.datasource.password}")
    private String password;

	public static void main(String[] args) {
		SpringApplication.run(PropertyEncryptionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("password:" + password);
		
	}

}
