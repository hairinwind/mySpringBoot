This project is focusing on convert properties configuration to object directly.  

Run the project, the object is printed on console, like this 
```
users: PropertyConfig(name=John Terry, dateOfBirth=Thu Dec 31 00:00:00 PST 1970, country=CN, titles=[doctor, MR], nationalities=[USA, CN], addresses=[Address(streetAddress=123 kingsway, city=vancouver), Address(streetAddress=345 smith rd, city=burnaby)])
Address(streetAddress=123 kingsway, city=vancouver)
```
 
## @ConfigurationProperties
With this annoation, this java class is the mapping class of the properties. 
With the setting prefix = "user", it only picks the propertes starting with users, like users.dateOfBirth

## enum
the property can be converted to enum type automatically.
In property file
```
users.country: CN
```
In java class
```
private Country country;
```

## property to Date field
Like the property "dateOfBirth", I want to convert it to Date directly. It needs a customer convert, which is MyStringToDateConverter.  
It implements Converter<String, Date> and needs the annotation 
```
@Component  
@ConfigurationPropertiesBinding
```
The annotation @ConfigurationPropertiesBinding binds the custom converter to spring convertService. 

## List objects - short way  
If the list objects are short and they can be put in one line, here is the example 
```
users.titles: doctor, MR
```

## List objects - long way
if one item in list is quite long, here is the example
```
users.addresses[0]: {"streetAddress":"123 kingsway", "city":"vancouver"}
users.addresses[1]: {"streetAddress":"345 smith rd", "city":"burnaby"}
```

## Set objects, similar to list objects
in properties file 
```
users.nationalities: CN, USA, CN
```
In Java
```
private Set<Country> nationalities = new HashSet<>();
```

## json string to object
```
users.addresses[0]: {"streetAddress":"123 kingsway", "city":"vancouver"}
```
convert it to sub object directly
```
private List<Address> addresses = new ArrayList<>();
```
i found it had to use yml, then it can convert json in addresses directly to POJO. 

## TODO 
https://spring.io/blog/2018/03/28/property-binding-in-spring-boot-2-0

