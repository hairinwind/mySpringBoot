package my.springdata.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "departments")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Department.withEmployee",
        attributeNodes = {
            @NamedAttributeNode(value="departmentEmployees", subgraph="employee")
        }, 
        subgraphs = {
        		@NamedSubgraph(name = "employee", attributeNodes = @NamedAttributeNode("employee"))
        }
    ),
    @NamedEntityGraph(
            name = "Department.withManager",
            attributeNodes = {
                @NamedAttributeNode("managers")
            }
    )
})
//this is to solve json endless loop
//Depart -> DepartmentEmployee -> Department
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "deptNo")
public class Department {
	@Id
	private String deptNo;
	private String deptName;
	@OneToMany(mappedBy="department", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	private Set<DepartmentEmployee> departmentEmployees;
	
	// I changed the data model and make from_date, to_date nullable
	// so that we can ignore those two columns and make the table dept_manager a pure join table
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="dept_manager", joinColumns = @JoinColumn(name = "dept_no"),
				inverseJoinColumns = @JoinColumn(name = "emp_no"))
	private Set<Employee> managers;
	
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Set<DepartmentEmployee> getDepartmentEmployees() {
		return departmentEmployees;
	}
	public void setDepartmentEmployees(Set<DepartmentEmployee> departmentEmployees) {
		this.departmentEmployees = departmentEmployees;
	}
	public Set<Employee> getManagers() {
		return managers;
	}
	public void setManagers(Set<Employee> managers) {
		this.managers = managers;
	}
}
