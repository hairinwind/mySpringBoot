package my.springboot.requestbody.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is to test rest requestBody mapping to java pojo
 * https://www.baeldung.com/jackson-nested-values
 *
 */
@RestController
public class AppController {
	 
	/**
	 * This is a straight forward example, the requestBody json is exactly matching the domain objects
	 * no mapping need explicitly set
	 * @param customer
	 * @return
	 */
	@PostMapping("/updateCustomer")
    public Customer updateCustomer(@Valid @RequestBody Customer customer) {
		System.out.println("..customer..." + customer.toString());
        return customer;
    }
	
}
