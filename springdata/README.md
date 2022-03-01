# My practice project for spring boot and spring data JPA

## data model
https://dev.mysql.com/doc/employee/en/employees-installation.html

https://bitbucket.org/bonbonniere/springdata/src/2b018efb454e3b5e15cafa3dafc523ff53f7fc80/employees-schema.png?at=master

## ManyToOne
See my.springdata.model.EmployeeTitle

## OneToMany bidirection
See my.springdata.model.Employee titles  
mappedBy the property name on the "Many" object  
Here, employee is "One" and titles is "Many", which type is Title. In the title object, the relationship is on the property "employee". That is mappedBy="employee" coming from.

## OneToMany Unidirection
See my.springdata.model.Employee salaries, need @JoinColumn

## ManyToMany implemented with two ManyToOne/OneToMany
For the relationship betwen employee and department, the joint table has from_date and to_date to keep the history data. This cannot be done by ManyToMany directly. It can be done by two ManyToOne/OneToMany association. One is from Employee to the DepartEmployee. The other one is from Department to DepartmentEmployee. 

## ManyToMany 
I change the from_data and to_date to be null on the joint table dept_manager. Basically the purpose is to ignore those two fields and consider the table as a pure joint table. 

See my.springdata.model.Department managers

This is a unidirection ManyToMany map.

## Entity with composite Id
This is a legacy data model still using composite id. 

See my.springdata.model.EmployeeTitle

@IdClass need gerneate a class for the key EmployeeTitleKey.class

## Entity field is Enum
See my.springdata.model.Employee Gender  
my.springdata.model.Gender

## XyzRepository is Spring Data DAO 
most time only need provide method declaration and no need to implement it

See my.springdata.dao.DepartmentRepository  
my.springdata.dao.EmployeeRepository

## Scan respository in other packages
@EnableJpaRepositories("my.springdata.dao")

see my.springdata.Application

## Add custom method to Repository
https://www.mkyong.com/spring-data/spring-data-add-custom-method-to-repository/

See my.springdata.dao.EmployeeRepositoryCustomer  
my.springdata.dao.EmployeeRepository  
my.springdata.dao.EmployeeRepositoryImpl

## for complicated persistence logic, create a Dao class 
See my.springdata.dao.EmployeeDao

For example, add update salary in this data model is actually updating the to_date form 9999-12-31 to the new salary start date. Then add a new salary record. 

## Fetch lazy by @NamedEntityGraphs, @NamedEntityGraph and @EntityGraph
On Entity Department
```
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Department.withEmployee",
        attributeNodes = {
            @NamedAttributeNode(value="departmentEmployees", subgraph="employee")
        }, 
        subgraphs = {
        		@NamedSubgraph(name = "employee", attributeNodes = @NamedAttributeNode("employee"))
        }
    )
```
On Repository DepartmentRepository
```
@EntityGraph(value = "Department.withEmployee", type = EntityGraphType.LOAD)
List<Department> findWithEmployeeByDeptNo (String deptNo);
```   
I found fetch=FETCH.EAGER does not mean spring will use join query. And FetchMode.JOIN is isgnored by Sping JPA.

https://stackoverflow.com/questions/29602386/how-does-the-fetchmode-work-in-spring-data-jpa

http://www.radcortez.com/jpa-entity-graphs/

## JPQL query JOIN FETCH to load lazy 
See my.springdata.dao.EmployeeRepository.findEmployeeWithCurrentTitleAndSalaryById

CURRENT_DATE is the sysdate, now() in JPQL

FETCH lazy has two ways to implement. One is @EntityGraph, the other one is JPQL JOIN FETCH. To me, I prefer to the JPQL.

## Exception: cannot simultaneously fetch multiple bags:
to avoid exception 'cannot simultaneously fetch multiple bags:' the collection of OneToMany has to be set to avoid duplicated objects

See my.springdata.dao.EmployeeRepository findEmployeeWithCurrentTitleAndSalaryById

https://stackoverflow.com/questions/17566304/multiple-fetches-with-eager-type-in-hibernate-with-jpa
http://blog.eyallupu.com/2010/06/hibernate-exception-simultaneously.html

## oneToMany sorted collection 
See my.springdata.model.Employee salaries
```
@OrderBy("fromDate ASC")
private Set<Salary> salaries;
```
No worry about the type Set, the framework will automatically use LinkedHashSet

## Cascade
See my.springdata.controller.EmployeeController addNewEmployee  
my.springdata.controller.EmployeeController deleteEmployee  
my.springdata.dao.EmployeeDao changeEmployeeTitle

## transaction
On the repository interface, put the annotation 
```
@Transactional(readOnly = true)
```
For each method need write the database, put these annotations on the method level
```
@Modifying
@Transactional
```
logging transaction
See application.properties  
```
logging.level.org.springframework.transaction=DEBUG
logging.level.org.springframework.orm.jpa=DEBUG
```
Here are the logs that the transaction was rolled back
```
...
Rolling back JPA transaction on EntityManager [org.hibernate.jpa.internal.EntityManagerImpl@73c1147f]
Closing JPA EntityManager [org.hibernate.jpa.internal.EntityManagerImpl@73c1147f] after transaction
Closing JPA EntityManager
Could not complete request
```

## Spring data JPA save method may have performance issue if session is closed
See my.springdata.controller.DepartmentContoller.addManagerToDepartment

I actually only need one insert sql statement on the joint table. When calling save, if the session is closed, spring did serveral queries. It could be N+1 queries here. 

## native query
See my.springdata.dao.DepartmentRepository.addManager

## Respository method can return List, Set or Optional Or the Entity if only one object is expected 
See my.springdata.dao.DepartmentRepository.findWithManagerByDeptNo

## @PageableDefault
See my.springdata.controller.DepartmentContoller.findWithLimitedEmployeeByCode

## @JsonIdentityInfo to avoid endless loop in JSON
See EmployeeTitle

It cannot use composite id so generate a seiralizedId. Using JSON string also works, as long as having a unique id. 

http://detailfocused.blogspot.ca/2017/10/jpa-bidirection-mapping-json-infinite.html

## Avoid Jackson to serialize the lazy load fields 
Add this in pom.xml
```
<dependency>
	<groupId>com.fasterxml.jackson.datatype</groupId>
	<artifactId>jackson-datatype-hibernate5</artifactId>
	<version>2.9.2</version>
</dependency>
```
see 
AppConfig  
EmployeeController.findById()

https://stackoverflow.com/questions/33727017/configure-jackson-to-omit-lazy-loading-attributes-in-spring-boot
https://github.com/FasterXML/jackson-datatype-hibernate

## Jsr310JpaConverters and LocalDate
See my.springdata.model.Employee hireDate and birthDate

To convert it correctly in JSON

add this in pom.xml
```
<dependency>
	<groupId>com.fasterxml.jackson.datatype</groupId>
	<artifactId>jackson-datatype-jsr310</artifactId>
	<version>2.9.2</version>
</dependency>
```
add this in application.properties
```
spring.jackson.serialization.write_dates_as_timestamps=false
```
Now the localDate in json is "birthDate": "1953-09-02"

https://stackoverflow.com/questions/29956175/json-java-8-localdatetime-format-in-spring-boot

## @RequestBody and error "Content type 'application/json;charset=UTF-8' not supported"
Spring boot already support application/json. No extra setting is needed here. The error might be caused by the error while converting @RequestBody to the expected java Object.

Here is the log
```
Failed to evaluate Jackson deserialization for type [[simple type, class my.springdata.model.Department]]: com.fasterxml.jackson.databind.JsonMappingException: Invalid Object Id definition for my.springdata.model.DepartmentEmployee: can not find property with name 'serializedId'
```
See my.springdata.controller.EmployeeController.addNewEmployee  
and the serializedId in DepartmentEmployee and EmployeeTitle


## Spring boot configuration - application.properties
* don't open JPA entity Manager
* datasource
* dbcp
* hibernate logging

## References
https://docs.spring.io/spring-data/jpa/docs/2.0.0.RELEASE/reference/html/#repositories.query-methods

spring boot tutorial online: http://www.mkyong.com/tutorials/spring-boot-tutorials/


