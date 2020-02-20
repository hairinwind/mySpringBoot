## myFeign project
this project is to test different configuration about feign.

## change data before send by Feign
Feign allows you to config the encoder. In this project, I create MySpringEncoder which can change the data before sending.

## Interceptors
Interceptors can be injected to change head values, etc. 
Interceptors can be declared as @Bean or in application.yml

## logging
To turn on the detail logging, put both of these in application.yml
```
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: FULL

logging.level.my.springboot.feign.JSONPlaceHolderClient: DEBUG
```