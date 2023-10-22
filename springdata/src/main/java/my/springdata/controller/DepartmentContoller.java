package my.springdata.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import my.springdata.dao.DepartmentEmployeeRepository;
import my.springdata.dao.DepartmentRepository;
import my.springdata.dao.EmployeeRepository;
import my.springdata.model.Department;
import my.springdata.model.DepartmentEmployee;
import my.springdata.model.Employee;

@RestController
@RequestMapping("/department")
public class DepartmentContoller {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private DepartmentEmployeeRepository departmentEmployeeRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@RequestMapping("/findAll")
	public ResponseEntity<?> findAllDepartments() {
		return ResponseEntity.ok(departmentRepository.findAll());  
	}
	
	@RequestMapping("/findByName")
	public ResponseEntity<?> findByName(@RequestParam(value="name", required=true) String name) {
		return ResponseEntity.ok(departmentRepository.findByDeptName(name));  
	}
	
	@RequestMapping("/findByCode/{code}")
	public ResponseEntity<?> findByCode(@PathVariable String code) {
		return ResponseEntity.ok(departmentRepository.findByDeptNo(code));  
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> addNewDepartment(@RequestBody Department department) {
		return ResponseEntity.ok(departmentRepository.save(department));
	}
	
	@DeleteMapping("/{code}")
	public ResponseEntity<?> addNewDepartment(@PathVariable String code) {
		departmentRepository.delete(code);
		return ResponseEntity.ok("Department " + code + " was deleted");
	}
	
	// TEST URL: http://localhost:8080/department/findWithEmployeeByCode/d001
	@RequestMapping("/findWithEmployeeByCode/{code}")
	public ResponseEntity<?> findWithEmployeeByCode(@PathVariable String code) {
		long start = System.currentTimeMillis();
		List<Department> department = departmentRepository.findWithEmployeeByDeptNo(code);
		long end = System.currentTimeMillis();
		System.out.println("\n time for findWithEmployeeByCode: " + (end-start)/1000 + "s\n");
		//the query only took 3 seconds
		//most time was used by converting to JSON
		return ResponseEntity.ok(department);
	}
	
	@RequestMapping("/findWithLimitedEmployeeByCode/{code}")
	public ResponseEntity<?> findWithLimitedEmployeeByCode(@PathVariable String code,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		Set<Department> department = departmentRepository.findByDeptNo(code);
//		Pageable pageable = new PageRequest(0, 5);		
//		List<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository.findWithEmployeeByDeptNo(code, pageable);
		List<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository.findWithEmployeeByDeptNoByJPQL(code, pageable);
		department.iterator().next().setDepartmentEmployees(new HashSet<DepartmentEmployee>(departmentEmployees));
		return ResponseEntity.ok(department);
	}
	
	@RequestMapping("/findWithPagedEmployeeByCode/{code}")
	public ResponseEntity<?> findWithPagedEmployeeByDeptNo(@PathVariable String code,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		Page<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository.findWithPagedEmployeeByDeptNoByJPQL(code, pageable);
		// the Page object contains the total records number, total page number, page size...
		// departmentEmployees.getContent() is the list result
		return ResponseEntity.ok(departmentEmployees);
	}
	
	@RequestMapping("/findWithManagerByDeptNo/{code}")
	public ResponseEntity<?> findWithManagerByDeptNo(@PathVariable String code) {
		return ResponseEntity.ok(departmentRepository.findWithManagerByDeptNo(code));
	}
	
	@PostMapping("/addManager/{code}/{empNo}")
	@Modifying
	@Transactional
	public ResponseEntity<?> addManagerToDepartment(@PathVariable String code, @PathVariable int empNo) {
//		TODO this approach is not perfect as it is doing two queries first
//		actually it only need to execute an update which does not need these two queries.
//		
//		Without the annotation @Modifying and @Transactional, the transaction is started/closed at the dao level. 
//		which means in controller, the session is already closed. 
//		it will generate extra queries
// 		Here are the sql statements from the log
//		There are a couple select queries happend one by one after 'save', which is not good
//		select department0_.dept_no as dept_no1_0_0_, employee2_.emp_no as emp_no1_3_1_, department0_.dept_name as dept_nam2_0_0_, employee2_.birth_date as birth_da2_3_1_, employee2_.first_name as first_na3_3_1_, employee2_.gender as gender4_3_1_, employee2_.hire_date as hire_dat5_3_1_, employee2_.last_name as last_nam6_3_1_, managers1_.dept_no as dept_no1_2_0__, managers1_.emp_no as emp_no2_2_0__ from departments department0_ left outer join dept_manager managers1_ on department0_.dept_no=managers1_.dept_no left outer join employees employee2_ on managers1_.emp_no=employee2_.emp_no where department0_.dept_no=?
//		Adding transactional method 'findOne' with attribute: PROPAGATION_REQUIRED,ISOLATION_DEFAULT,readOnly; ''
//		select employee0_.emp_no as emp_no1_3_0_, employee0_.birth_date as birth_da2_3_0_, employee0_.first_name as first_na3_3_0_, employee0_.gender as gender4_3_0_, employee0_.hire_date as hire_dat5_3_0_, employee0_.last_name as last_nam6_3_0_ from employees employee0_ where employee0_.emp_no=?
//		Adding transactional method 'save' with attribute: PROPAGATION_REQUIRED,ISOLATION_DEFAULT; ''
//		select department0_.dept_no as dept_no1_0_1_, department0_.dept_name as dept_nam2_0_1_, department1_.dept_no as dept_no1_1_3_, department1_.emp_no as emp_no2_1_3_, department1_.dept_no as dept_no1_1_0_, department1_.emp_no as emp_no2_1_0_, department1_.from_date as from_dat3_1_0_, department1_.to_date as to_date4_1_0_ from departments department0_ left outer join dept_emp department1_ on department0_.dept_no=department1_.dept_no where department0_.dept_no=?
//		select employee0_.emp_no as emp_no1_3_1_, employee0_.birth_date as birth_da2_3_1_, employee0_.first_name as first_na3_3_1_, employee0_.gender as gender4_3_1_, employee0_.hire_date as hire_dat5_3_1_, employee0_.last_name as last_nam6_3_1_, department1_.emp_no as emp_no2_1_3_, department1_.dept_no as dept_no1_1_3_, department1_.dept_no as dept_no1_1_0_, department1_.emp_no as emp_no2_1_0_, department1_.from_date as from_dat3_1_0_, department1_.to_date as to_date4_1_0_ from employees employee0_ left outer join dept_emp department1_ on employee0_.emp_no=department1_.emp_no where employee0_.emp_no=?
//		select employee0_.emp_no as emp_no1_3_1_, employee0_.birth_date as birth_da2_3_1_, employee0_.first_name as first_na3_3_1_, employee0_.gender as gender4_3_1_, employee0_.hire_date as hire_dat5_3_1_, employee0_.last_name as last_nam6_3_1_, department1_.emp_no as emp_no2_1_3_, department1_.dept_no as dept_no1_1_3_, department1_.dept_no as dept_no1_1_0_, department1_.emp_no as emp_no2_1_0_, department1_.from_date as from_dat3_1_0_, department1_.to_date as to_date4_1_0_ from employees employee0_ left outer join dept_emp department1_ on employee0_.emp_no=department1_.emp_no where employee0_.emp_no=?
//		select employee0_.emp_no as emp_no1_3_1_, employee0_.birth_date as birth_da2_3_1_, employee0_.first_name as first_na3_3_1_, employee0_.gender as gender4_3_1_, employee0_.hire_date as hire_dat5_3_1_, employee0_.last_name as last_nam6_3_1_, department1_.emp_no as emp_no2_1_3_, department1_.dept_no as dept_no1_1_3_, department1_.dept_no as dept_no1_1_0_, department1_.emp_no as emp_no2_1_0_, department1_.from_date as from_dat3_1_0_, department1_.to_date as to_date4_1_0_ from employees employee0_ left outer join dept_emp department1_ on employee0_.emp_no=department1_.emp_no where employee0_.emp_no=?
//		select managers0_.dept_no as dept_no1_2_0_, managers0_.emp_no as emp_no2_2_0_, employee1_.emp_no as emp_no1_3_1_, employee1_.birth_date as birth_da2_3_1_, employee1_.first_name as first_na3_3_1_, employee1_.gender as gender4_3_1_, employee1_.hire_date as hire_dat5_3_1_, employee1_.last_name as last_nam6_3_1_ from dept_manager managers0_ inner join employees employee1_ on managers0_.emp_no=employee1_.emp_no where managers0_.dept_no=?
//		insert into dept_manager (dept_no, emp_no) values (?, ?)
//		
//		add the annotation @Modifying and @Transactional, the transaction is started at the controller level 
//		test again, the log only has one insert, no extra sql 
//		select department0_.dept_no as dept_no1_0_0_, employee2_.emp_no as emp_no1_3_1_, department0_.dept_name as dept_nam2_0_0_, employee2_.birth_date as birth_da2_3_1_, employee2_.first_name as first_na3_3_1_, employee2_.gender as gender4_3_1_, employee2_.hire_date as hire_dat5_3_1_, employee2_.last_name as last_nam6_3_1_, managers1_.dept_no as dept_no1_2_0__, managers1_.emp_no as emp_no2_2_0__ from departments department0_ left outer join dept_manager managers1_ on department0_.dept_no=managers1_.dept_no left outer join employees employee2_ on managers1_.emp_no=employee2_.emp_no where department0_.dept_no=?
//		select employee0_.emp_no as emp_no1_3_0_, employee0_.birth_date as birth_da2_3_0_, employee0_.first_name as first_na3_3_0_, employee0_.gender as gender4_3_0_, employee0_.hire_date as hire_dat5_3_0_, employee0_.last_name as last_nam6_3_0_ from employees employee0_ where employee0_.emp_no=?
//		insert into dept_manager (dept_no, emp_no) values (?, ?)
		Optional<Department> department = departmentRepository.findWithManagerByDeptNo(code);
		System.out.println("after findWithManagerByDeptNo...");
		Employee manager = employeeRepository.findOne(empNo);
		System.out.println("after findEmployee...");
		if (manager != null) {
			Optional<Department> savedDepatarment = department.map(dept -> {
				dept.getManagers().add(manager);
				departmentRepository.save(dept);
				return dept;
				});
			return ResponseEntity.ok(savedDepatarment.orElse(department.orElse(null)));
		}
		return ResponseEntity.ok(department.orElseGet(null));
	}
	
	@PostMapping("/addManagerNative/{code}/{empNo}")
	public ResponseEntity<?> addManagerToDepartmentNative(@PathVariable String code, @PathVariable int empNo) {
		departmentRepository.addManager(code, empNo);
		return ResponseEntity.ok(departmentRepository.findWithManagerByDeptNo(code)
				.orElseThrow(()-> new RuntimeException("Department not found for " + code)));
	}
	
	@DeleteMapping("/removeManagerNative/{code}/{empNo}")
	public ResponseEntity<?> removeManagerFromDepartmentNative(@PathVariable String code, @PathVariable int empNo) {
		departmentRepository.removeManager(code, empNo);
		return ResponseEntity.ok(departmentRepository.findWithManagerByDeptNo(code).orElse(null));
	}
}
