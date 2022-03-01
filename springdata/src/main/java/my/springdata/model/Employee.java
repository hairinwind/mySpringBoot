/**
 * @JsonIdentityInfo for avoid JSON infinite loop
 * gender is enum type
 *  
 */
package my.springdata.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "employees")

// @JsonIdentityInfo is to resolve cyclic dependencies in an object graph by using an ID/reference mechanism 
// so that an object instance is only completely serialized once and referenced by its ID elsewhere.
// http://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "empNo")

// NamedEntityGraph for loading the lazy load collections
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Employee.withTitle",
        attributeNodes = {
            @NamedAttributeNode("titles")
        }
    ),
    //cannot have titles and salaries in one graph, no sql can work for that
    //the error will be 'cannot simultaneously fetch multiple bags:' 
    @NamedEntityGraph(
            name = "Employee.withSalary",
            attributeNodes = {
                @NamedAttributeNode("salaries")
            }
        ),
    @NamedEntityGraph(
            name = "Employee.withDepartment",
            attributeNodes = {
                @NamedAttributeNode(value="departmentEmployees", subgraph="department")
            }, 
            subgraphs = {
            	@NamedSubgraph(name = "department", attributeNodes = @NamedAttributeNode("department"))
            }
        )
})
public class Employee {
	@Id
	private int empNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	private String firstName;
	private String lastName;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate hireDate;
	
	@OneToMany(mappedBy="employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	// This is bidirection mapping, the mapping detail is on the EmployeeTitle side.
	@OrderBy("fromDate ASC")
	private Set<EmployeeTitle> titles;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	// This is unidirection mapping, so it has to declare the JoinColumn. 
	// Otherwise, JPS throws an strange error "java.sql.SQLSyntaxErrorException: Table 'employees.employees_salaries' doesn't exist"
	// https://vladmihalcea.com/2017/03/29/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
	@JoinColumn(name = "emp_no")
	@OrderBy("fromDate ASC")
	private Set<Salary> salaries;
	
	@OneToMany(mappedBy="employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	private Set<DepartmentEmployee> departmentEmployees;
	
	public int getEmpNo() {
		return empNo;
	}
	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public LocalDate getHireDate() {
		return hireDate;
	}
	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}
	public Set<EmployeeTitle> getTitles() {
		return titles;
	}
	public void setTitles(Set<EmployeeTitle> titles) {
		this.titles = titles;
	}
	public Set<Salary> getSalaries() {
		return salaries;
	}
	public void setSalaries(Set<Salary> salaries) {
		this.salaries = salaries;
	}
	public Set<DepartmentEmployee> getDepartmentEmployees() {
		return departmentEmployees;
	}
	public void setDepartmentEmployees(Set<DepartmentEmployee> departmentEmployees) {
		this.departmentEmployees = departmentEmployees;
	}
}
