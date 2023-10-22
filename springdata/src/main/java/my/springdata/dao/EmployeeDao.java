package my.springdata.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.springdata.model.Employee;
import my.springdata.model.EmployeeTitle;
import my.springdata.model.Salary;

@Repository
public class EmployeeDao {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Modifying
	@Transactional
	/**
	 * This function need find the latest salary and update its to_date to the current date
	 * and add the new salary to the salary collection and persist it
	 * @param empNo
	 * @param newSalary
	 */
	public void changeEmployeeSalary(int empNo, Salary newSalary) {
		Employee employee = employeeRepository.findEmployeeWithSalariesByEmpNo(empNo);
		
		//check if hibernate session is closed? 
//		Set<EmployeeTitle> titles = employee.getTitles();
//		System.out.println("titles size: " + titles.size());
		
//		List<Salary> salaries = new ArrayList<>(employee.getSalaries());
//		Salary lastSalary = salaries.get(salaries.size() - 1);
//		System.out.println(lastSalary.getToDate());
//		lastSalary.setToDate(new Date());
		// TODO this does not work
		// here is the excption 
		// "exception": "org.springframework.orm.ObjectOptimisticLockingFailureException",
	    // "message": "Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1;
		Set<Salary> salarySet = employee.getSalaries();
		salarySet.add(newSalary);
		employee.setSalaries(salarySet);
		employeeRepository.save(employee);
	}
	
	@Modifying
	@Transactional
	public void changeEmployeeTitle(int empNo, EmployeeTitle newTitle) {
		Employee employee = employeeRepository.findEmployeeWithTitleByEmpNo(empNo);
		// update the last titles item to_date
		List<EmployeeTitle> titles = new ArrayList<>(employee.getTitles());
		EmployeeTitle lastTitle = titles.get(titles.size() -1);
		lastTitle.setToDate(newTitle.getFromDate());
		// add new title item
		employee.getTitles().add(newTitle);
		employeeRepository.save(employee);
	}
}
