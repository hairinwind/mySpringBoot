## add dependencies in pom.xml
```
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>${jasypt.version}</version>
</dependency>

<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot</artifactId>
    <version>${jasypt.version}</version>
</dependency>
```

## add annotation to enable encryptable properties
```
@EnableEncryptableProperties
public class PropertyEncryptionApplication implements CommandLineRunner {
	...
```

## encrypt sensitive properties
https://medium.com/@sun30nil/how-to-secure-secrets-and-passwords-in-springboot-90c952961d9
```
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=Topsecret@123 password=dev-env-secret algorithm=PBEWITHMD5ANDDES
```
This encrypts "Topsecret@123" with the key "dev-env-secret" and it outputs "U5ky3iDGTZkR+L1bUGjMiKKaE8J2U3TX".  
Each time you run it, it returns different encrypted text.

## put the encrypted text into property file 
```
spring.datasource.password=ENC(U5ky3iDGTZkR+L1bUGjMiKKaE8J2U3TX)
```

## set java vm argument
```
-Djasypt.encryptor.password=dev-env-secret
```

Now the spring can decrypt the encrypted properties

https://www.baeldung.com/spring-boot-jasypt

## disable bean overriding
In AnotherConfiguration, create the same bean "sampleBean". If you don't want to allow bean overriding, put the property below
```
spring.main.allow-bean-definition-overriding=false
```
Then, you will get the exception. 
I used to have two jasypt bean in my project. They are duplicated. Later, I upgraded the jasypt to another algorithm. But it did not work. It is because there is old bean override the jasypt bean I upgraded. 
Setting the property above can avoid bean overriding. 

The two different beans have to be configured in different configuration classes. If they are on the same class, it won't throw error. 
