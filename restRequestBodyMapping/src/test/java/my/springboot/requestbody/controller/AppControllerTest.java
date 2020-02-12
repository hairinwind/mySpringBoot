package my.springboot.requestbody.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import my.springboot.requestbody.RestRequestBodyMappingApplication;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes = RestRequestBodyMappingApplication.class)
@AutoConfigureMockMvc
class AppControllerTest {

	@Autowired
	MockMvc mvc;
	
	@LocalServerPort
	int randomServerPort;
	
	@Test
	void test() throws Exception {
		log.info("... testEnv runtime localServicePort: {}", randomServerPort);
		
		Product product1 = Product.builder()
				.brand(Brand.builder().id("brand1").owner(Owner.builder().id("1").name("owner1").build()).build())
				.id(987654321)
				.build();
		Product product2 = Product.builder()
				.brand(Brand.builder().id("brand2").owner(Owner.builder().id("2").name("owner2").build()).build())
				.id(987654322)
				.build(); 
		List<Product> products = Arrays.asList(product1, product2);
		Customer customer = Customer.builder()
				.id("customer_id_1")
				.name("customer1")
				.dateOfBirth(LocalDate.of(2000, 1, 20))
				.gender(Gender.MALE)
				.nationalitySet(new HashSet<>(Arrays.asList(Country.CA, Country.USA)))
				.products(products)
				.build();
		mvc.perform(MockMvcRequestBuilders.post("/updateCustomer")
					.content(asJsonString(customer))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(customer.getId()))
				.andExpect(jsonPath("$.gender").value(Gender.MALE.name()))
				.andExpect(jsonPath("$.dateOfBirth").value("2000-01-20"))
				.andExpect(jsonPath("$.products[0].brand.owner.name").value("owner1"))
				.andExpect(jsonPath("$.nationalitySet").value(IsIterableContaining.hasItem("CA")))
				.andExpect(jsonPath("$.nationalitySet").value(IsIterableContaining.hasItem("USA")))
				;
				
		assertTrue(true);
	}

	private String asJsonString(Customer customer) {
		try {
			String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(customer);
			log.info("...jsonString {} ", jsonString);
			return jsonString;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	void testRequiredCustomerIdNotProvided() throws Exception {
		Customer customer = Customer.builder().name("cusotmer1").build();
		mvc.perform(MockMvcRequestBuilders.post("/updateCustomer")
					.content(asJsonString(customer))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());
		
		//TODO shall get validation error
	}

}
