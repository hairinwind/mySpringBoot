package my.springboot.property;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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
		
	@Value("#{new java.text.SimpleDateFormat('yyyy-MM-dd').parse('${users.dateOfBirth}')}")
	private Date dateOfBirth;
	
	@Value("${users.dateOfBirth}")
	@ToString.Exclude
	private String dateOfBirthText;
	
	private LocalDate localDateOfBirth;

	private Country country;
	
	private List<String> titles = new ArrayList<>();
	
	private List<Country> nationalities = new ArrayList<>();
	
	private List<Address> addresses = new ArrayList<>();

	@PostConstruct
	private void postConstruct() {
		System.out.println("users: " + this);
		this.addresses.forEach(System.out::println);
	}
	
	public LocalDate getLocalDateOfBirth() {
		return LocalDate.parse(this.dateOfBirthText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
