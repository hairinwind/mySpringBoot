package my.springboot.requestbody.controller;

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
    public Customer updateCustomer(@RequestBody Customer customer) {
		System.out.println("..customer..." + customer.toString());
        return customer;
    }
	
	@PostMapping("/updateCustomer1")
	public my.springboot.requestbody.domain1.Customer updateCustomer1(
			@RequestBody my.springboot.requestbody.domain1.Customer customer) {
		System.out.println("...customer..." + customer.toString());
		return customer;
	}
}
