package my.springboot.lib.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import my.springboot.lib.feign.JSONPlaceHolderClient;

@Configuration
@EnableFeignClients(clients = {JSONPlaceHolderClient.class})
@EnableAutoConfiguration
@ComponentScan
public class SpringBootLibConfig {
	

}
