package my.springdata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "dept_emp")
@IdClass(DepartmentEmployeeKey.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "serializedId")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "DepartmentEmployee.withEmployee",
        attributeNodes = {
            @NamedAttributeNode(value="employee")
        }
    )
})
public class DepartmentEmployee {
	@Id
	@Column(name = "emp_no")
	private int empNo;
	@Id
	@Column(name = "dept_no")
	private String deptNo;
	private Date fromDate;
	private Date toDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_no", insertable = false, updatable = false)
	private Employee employee;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_no", insertable = false, updatable = false) //referencedColumnName="dept_no" the join column in the other table
	private Department department;
	
	public int getEmpNo() {
		return empNo;
	}
	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	//This property has to be provide, otherwise you will get error when map requestBody to this object
	//The error is like Failed to evaluate Jackson deserialization for type [[simple type, class my.springdata.model.Department]]: com.fasterxml.jackson.databind.JsonMappingException: Invalid Object Id definition for my.springdata.model.DepartmentEmployee: can not find property with name 'serializedId'
	@Transient
	private String serializedId;
	public String getSerializedId() {
		DepartmentEmployeeKey key = new DepartmentEmployeeKey(this.getEmpNo(), this.getDeptNo());
		return key.serialize();
	}
	
	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
}
