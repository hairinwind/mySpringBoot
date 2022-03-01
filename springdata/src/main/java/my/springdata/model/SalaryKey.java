package my.springdata.model;

import java.io.Serializable;
import java.util.Date;

public class SalaryKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private int empNo;
	private Date fromDate;
	public int getEmpNo() {
		return empNo;
	}
	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
}
