
## create keypair
```$xslt
keytool -genkeypair -dname "cn=localhost, ou=Java, o=bonbon, c=CA" -alias localhost -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore localhost.p12 -validity 3650 -storepass changeit
```

## print
```$xslt
keytool -list -v -keystore localhost.p12 -storepass changeit
```

## client with trust.store
The SslApplicationTests shows the rest client to use trust.store.

## export crt
For the rest client who wants to call with JRE cacerts, It needs to export the cert from the keystore and import it into cacerts.
The crt contains the public key which can be used to encrypt data.
```$xslt
keytool -export -keystore localhost.p12 -alias localhost -file localhost.crt -storepass changeit
```

## import crt into client JRE cacerts
copy the created crt file from above step to the JRE cacerts folder, and run the command below to import it
```$xslt
keytool -importcert -file localhost.crt -alias localhost -keystore cacerts -storepass changeit
```

https://www.baeldung.com/spring-boot-https-self-signed-certificate

https://github.com/eugenp/tutorials/tree/master/spring-security-mvc-boot

