package my.springboot.requestbody.domain1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import my.springboot.requestbody.controller.Brand;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

	private String id;
	private String name;
	private Brand brand;
	
}
