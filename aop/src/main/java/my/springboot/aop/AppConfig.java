package my.springboot.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRetry
public class AppConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    public RetryTemplate exponentialBackOffRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> includeExceptions = new HashMap<>();
        includeExceptions.put(SQLException.class, true);

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(2000);
        exponentialBackOffPolicy.setMultiplier(2.0);
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(10, includeExceptions);
        retryPolicy.setMaxAttempts(10);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

}
