package my.reactive.myreactive;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    public CommandLineRunner initConfig() {
        return (String... args) -> {
            System.out.println("run initConfig here...");
        };
    }
}
