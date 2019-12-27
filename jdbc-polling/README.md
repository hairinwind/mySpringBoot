## visit h2 database from web console
https://stackoverflow.com/questions/55830010/how-to-enable-h2-database-server-mode-in-spring-boot
```$xslt
spring.datasource.initialization-mode=always
spring.datasource.schema=classpath:h2_init.sql
spring.datasource.continue-on-error=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
In the log, you can see   
Starting embedded database: url='jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'

visit http://localhost:8080/h2-console/login.do  
change URL to jdbc:h2:mem:testdb  
After login, you can see the test table.

## TODO
create a user specified database name