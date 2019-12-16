package my.springboot.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RetryDelegateService {
    private static final Logger log = LoggerFactory.getLogger(RetryDelegateService.class);

    @Autowired
    private RetryService retryService;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private RetryTemplate exponentialBackOffRetryTemplate;

    // this is for demo purpose
    private int retryCount = 0;

    @Value("${maxRetryAttempts}")
    private int maxRetryAttemps;

    @Retryable(
            value = { SQLException.class },
//            maxAttempts = 2,
            maxAttemptsExpression = "${maxRetryAttempts}", //value is from property file
//            backoff = @Backoff(delay = 500)
            backoff = @Backoff(delayExpression = "${retry.backoff.delay}")
    )
    public String getDataRetry() throws SQLException {
        retryCount ++;
        log.info("... execute time {}", retryCount);
        if (retryCount < maxRetryAttemps) {
            // mimic the first two times, you got errors
            throw new SQLException("...test exception...");
        } else {
            log.info("... retried {} times, return correct data", retryCount);
            retryCount = 0;
            return retryService.getDataRetry();
        }
    }

    @Retryable
    public String getDataRecovery() throws SQLException {
        throw new SQLException("... testSQLException for dataRecovery");
    }
    @Recover
    public String getDataRecovery(SQLException sqlException) {
        log.info("...getDataRecovery() is executed");
        return "this is recover data";
    }

    public String getDataByRetryTemplate() throws SQLException {
        return retryTemplate.execute(arg0 -> {
            return this.getDataRetry();
            //the @Retryable annotation on getDataRetry() is not effective
            //as it is a internal invoke
        });
    }

    public String getDataByExponentialTemplate() throws SQLException {
        return exponentialBackOffRetryTemplate.execute(arg0 -> {
            return this.getDataRetry();
        });
    }
}
