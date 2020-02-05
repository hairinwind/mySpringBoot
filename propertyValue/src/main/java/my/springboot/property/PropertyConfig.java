package my.springboot.property;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;
import my.springboot.property.domain.Address;
import my.springboot.property.domain.Country;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "users")
public class PropertyConfig {
	
	private String name;
	
	private Country country;
	
	private List<String> titles = new ArrayList<>();
	
	private List<Address> addresses = new ArrayList<>();

}
