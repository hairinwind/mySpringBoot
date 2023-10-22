package my.springdata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
public class AppConfig {
	
	/**
	 * This is to avoid Jackson to serialize the lazy load fields
	 * see EmployeeController.findById()
	 * https://stackoverflow.com/questions/33727017/configure-jackson-to-omit-lazy-loading-attributes-in-spring-boot
	 * https://github.com/FasterXML/jackson-datatype-hibernate
	 * @return
	 */
    @Bean
    public Module hibernate5Module()
    {
        return new Hibernate5Module();
    }
}