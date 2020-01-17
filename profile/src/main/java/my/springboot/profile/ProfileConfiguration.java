package my.springboot.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test") // this configuration does not work when profile is test
public class ProfileConfiguration {

	@Bean
	public SampleBean sampleBean() {
		return new SampleBean();
	}
	
}
