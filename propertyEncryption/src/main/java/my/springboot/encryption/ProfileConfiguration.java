package my.springboot.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileConfiguration {
	
	@Bean(name="sampleBean")
	public SampleBean firstSampleBean() {
		return new SampleBean("first sample Bean");
	}
	
}
