package my.springdata.model;

import java.io.Serializable;

public class DepartmentEmployeeKey extends AbstractKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private int empNo;
	private String deptNo;
	public DepartmentEmployeeKey() {
		
	}
	public DepartmentEmployeeKey(int empNo, String deptNo) {
		this.empNo = empNo;
		this.deptNo = deptNo;
	}
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
	
	
}
