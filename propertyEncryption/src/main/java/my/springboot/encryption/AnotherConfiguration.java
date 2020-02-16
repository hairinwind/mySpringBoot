package my.springboot.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnotherConfiguration {

	@Bean(name="sampleBean")
	public SampleBean secondSampleBean() {
		return new SampleBean("this is second sample bean");
	}
}
