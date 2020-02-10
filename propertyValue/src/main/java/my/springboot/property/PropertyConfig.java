package my.springboot.property;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

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
	
	private Date dateOfBirth;	

	private Country country;
	
	private List<String> titles = new ArrayList<>();
	
	private Set<Country> nationalities = new HashSet<>();
	
	private List<Address> addresses = new ArrayList<>();

	@PostConstruct
	private void postConstruct() {
		System.out.println("users: " + this);
		this.addresses.forEach(System.out::println);
	}

}
