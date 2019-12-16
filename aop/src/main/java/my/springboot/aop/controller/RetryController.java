package my.springboot.aop.controller;

import my.springboot.aop.service.RetryDelegateService;
import my.springboot.aop.service.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class RetryController {

    @Autowired
    private RetryDelegateService retryDelegateService;

    @GetMapping("/dataRetry")
    public String getData() throws SQLException {
        return retryDelegateService.getDataRetry();
    }

    @GetMapping("/dataRecovery")
    public String getDataRecovery() throws SQLException {
        return retryDelegateService.getDataRecovery();
    }

    @GetMapping("/dataByRetryTemplate")
    public String getDataByRetryTemplate() throws SQLException {
        return retryDelegateService.getDataByRetryTemplate();
    }

    @GetMapping("/dataByExponentialTemplate")
    public String getDataByExponentialTemplate() throws SQLException {
        return retryDelegateService.getDataByExponentialTemplate();
    }
}
