package my.springdata.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import my.springdata.model.Salary;

public interface EmployeeRepositoryCustomer {

	@Modifying
	@Transactional
	void changeEmployeeSalary(int empNo, Salary newSalary);
	
}
