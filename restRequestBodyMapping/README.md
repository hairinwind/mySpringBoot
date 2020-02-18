
## mapping json to java pojo (jackson)
See Cusotmer, most of them can be mapped automatically, like primitive, string and enum, also the list/set.
Here is the custom serialize/deserialize
```
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
@JsonDeserialize(using = LocalDateDeserializer.class)
@JsonSerialize(using = LocalDateSerializer.class)
private LocalDate dateOfBirth;
```

## validation shall be separated from mapping
https://dzone.com/articles/bean-validation-made-simple  
https://www.baeldung.com/javax-validation  
on Customer bean, add @NotNull annotation on id 
```
@NotNull
private String id;
```
On controller method, add @Valid on requestBody
```
@PostMapping("/updateCustomer")
public Customer updateCustomer(@Valid @RequestBody Customer customer) {
```
Now, if the customer in requestBody does not have id, the request returns 400 (Bad Request).

## validation on sub-object fields
set id of Product to be @NotNull
The @Valid has to be put in the list in parent object (Customer)
```
private List<@Valid Product> products = new ArrayList<>();
```
Check the AppControllerTest, it verifies the validations.

## regular expression validation
@Pattern
```
@Pattern(regexp = "[a-z-A-Z0-9]*", message = "name has invalid characters")
private String name;
```
@Email is already an annotation, you don't have to provide regex.

## validation dependency
e.g. endData greater than starDate  
here is one example https://www.baeldung.com/spring-mvc-custom-validator  
As it includes multiple fields, the annotation shall be on class level. See Customer
```
@EndDateConstraint.List({ 
    @EndDateConstraint(
      startDate = "startDate", 
      endDate = "endDate", 
      message = "endDate shall be after startDate"
    )
})
```
The implementation is EndDateConstraint and EndDateValidator.  
The unit test AppControllerTest verify that the validator works.