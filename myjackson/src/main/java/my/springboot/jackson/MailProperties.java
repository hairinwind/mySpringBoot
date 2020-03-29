package my.springboot.jackson;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailProperties {

	private String hostName;
	private int port;
	private String username;
	
	@JsonSerialize(using = JsonStringPartialSerializer.class)
	private String password;
}
