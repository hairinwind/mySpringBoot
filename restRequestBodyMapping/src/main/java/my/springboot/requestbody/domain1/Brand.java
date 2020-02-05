package my.springboot.requestbody.domain1;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import my.springboot.requestbody.controller.Owner;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Brand {

	private String id;
	private String name;
	
	@JsonProperty("owner")
	private List<Owner> owners = new ArrayList<>();
	
}
