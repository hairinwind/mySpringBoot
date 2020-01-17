package my.springboot.profile;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") //using the @ActiveProfile annotation to enable specific profiles
class ProfileApplicationTests {
	
	@Autowired
    private ApplicationContext appContext;

	@Test
	void contextLoads() {
		String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            if (bean.equals("sampleBean")) {
            	fail("sampleBean is found...");
            }
        }
	}

}
