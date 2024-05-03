package my.webflux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableAutoConfiguration
@OpenAPIDefinition // this is needed for swagger-ui
public class SpringWebfluxEssentialsApplication {
	static {
		BlockHound.install(
				// the blockHoud would throw out the error if there is block call in the flow
				// blockHoud would throw out the error
				// to bypass that error, add the code below
				builder -> builder.allowBlockingCallsInside("java.io.InputStream", "readNBytes")
						.allowBlockingCallsInside("java.io.FilterInputStream", "read")
						.allowBlockingCallsInside("org.springdoc.webflux.api.OpenApiWebfluxResource", "openapiJson")
		);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxEssentialsApplication.class, args);
	}

}
