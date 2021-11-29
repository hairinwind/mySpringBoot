This is the project to practise Spring boot and junit 5.

## don't load the entire application context
### Turn on the log to view bean creation
```
logging.level.org.springframework.beans.factory.support.DefaultListableBeanFactory=DEBUG
```
After this was turn on, search "Creating shared instance of singleton bean 'serviceA'" in the log

### default test
run my.spring.junit.service.ServiceATest  

Both serviceA and serviceB were initialized. When it is a big context, running all unit test will be slow.

### make the unit test only load the beans it needs
#### @SpringBootTest + @Configuration
See class my.spring.junit.service.ServiceA2ndTest  
If put @Configuration as an inner class, it has to be static class. Otherwise, it shall be created as another class. 

#### @Extend + @TestConfiguration
See class my.spring.junit.service.ServiceA3rdTest

These two solutions are equivalent.