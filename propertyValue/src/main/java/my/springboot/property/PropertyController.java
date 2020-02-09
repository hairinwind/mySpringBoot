package my.springboot.property;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import my.springboot.property.domain.Address;
import my.springboot.property.domain.Country;

@RestController
public class PropertyController {
	
	@Value("#{new java.text.SimpleDateFormat('yyyy-MM-dd').parse('${users.dateOfBirth}')}")
	private Date dateOfBirth;
	
	@Autowired 
	PropertyConfig propertyConfig;
	
	@GetMapping("/properties")
    public Date updateCustomer() {
		System.out.println("");
		System.out.println("..propertyConfig..." + propertyConfig.toString());
		System.out.println("");
		Country country = propertyConfig.getCountry();
		System.out.println("country: " + country.shortName() 
				+ " : " + country.longName());
		Address address1 = propertyConfig.getAddresses().get(0);
		System.out.println("address 1: " + address1.getStreetAddress() 
				+ " " + address1.getCity());
		System.out.println("dateOfBirth:" + dateOfBirth);
        return dateOfBirth;
    }
}
