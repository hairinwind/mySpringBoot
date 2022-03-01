/**
 * @IdClass composite primary key
 * @JsonIdentityInfo for avoid JSON infinite loop
 *  
 */
package my.springdata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "titles")
@IdClass(EmployeeTitleKey.class)
// As this entity uses composite primary key
// create a getSerializedId method to serialize the composite keys
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "serializedId")
public class EmployeeTitle {
	// composite ID
	// https://stackoverflow.com/questions/13032948/how-to-create-and-handle-composite-primary-key-in-jpa
	@Id
	@Column(name = "emp_no")
	private int empNo;
	@Id
	private String title;
	@Id
	private Date fromDate;

	private Date toDate;

	// it is ok to set it to be EAGER
	// the eager load from employee titles does not trigger more queries here to get
	// the tilte's employee
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "emp_no", insertable = false, updatable = false)
	private Employee employee;

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Transient
	private String serializedId;
	public String getSerializedId() {
		EmployeeTitleKey key = new EmployeeTitleKey(this.getEmpNo(), this.getTitle(), this.getFromDate());
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
