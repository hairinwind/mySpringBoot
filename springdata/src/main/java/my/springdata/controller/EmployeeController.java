package my.springdata.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import my.springdata.dao.EmployeeDao;
import my.springdata.dao.EmployeeRepository;
import my.springdata.dao.TitleRepository;
import my.springdata.model.Employee;
import my.springdata.model.EmployeeTitle;
import my.springdata.model.Salary;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private TitleRepository titleRepository;
	
	@RequestMapping("/test")
	public ResponseEntity<?> returnJson() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("name", "bill");
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping("/findByEmpNoAndFromDate")
	public ResponseEntity<?> findByEmpNoAndFromDate(
			@RequestParam(value="empNo", required=true) int empNo,
			//convert parameter to LocalData
			@RequestParam(value="fromDate", required=true)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		Date fromDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		//TODO use localDate directly in entity
		return ResponseEntity.ok(titleRepository.findByEmpNoAndFromDate(empNo, fromDate));
	}
	
	//findEmployeeTitleByEmpNameAndFromDate
	
	@GetMapping("/findById/{id}")
	@ApiOperation(value="findById", produces = "application/json")
	public ResponseEntity<Employee> findById(
			@ApiParam(defaultValue="10001", required = true) 
			@PathVariable Integer id) {
		Employee employee = employeeRepository.findOne(id);	
		// by default, Spring Boot registers an OpenEntityManagerInViewInterceptor, 
		// which ensures that the entity manager is open for the complete request, 
		// which means, the lazy collection is fetched while serializing the entity to JSON.
		// here, when JSON serializing, it calls getTitles method on employee which triggers another sql query
		// the extra sql query is expected and it can cause performance issue
		// to disable jpa entityManger open in the view 
		// add spring.jpa.open-in-view=false to application.properties.
		// now you will see the exception about "session is closed" as JSON serializing still call getTitles which throws that exception
		// the solution is to use the jackson-datatype-hibernate
		// https://stackoverflow.com/questions/28746584/how-to-avoid-lazy-fetch-in-json-serialization-using-spring-data-jpa-spring-web
		// check pom jackson-datatype-hibernate5
		// check AppConfig.hibernate5Module()
		// https://stackoverflow.com/questions/33727017/configure-jackson-to-omit-lazy-loading-attributes-in-spring-boot
		// now you will see titles is null in the JSON object
		return ResponseEntity.ok(employee);
	}
	
	/**
	 * eager load titles 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findEmployeeWithTitlesById/{id}")
	public ResponseEntity<?> findEmployeeWithTitlesById(@PathVariable Integer id) {
		return ResponseEntity.ok(employeeRepository.findEmployeeWithTitleByEmpNo(id));
	}
	
	/**
	 * eager load titles and salaries
	 * @param id
	 * @return
	 */
	@RequestMapping("/findEmployeeWithSalariesById/{id}")
	public ResponseEntity<?> findEmployeeWithSalariesById(@PathVariable Integer id) {
		return ResponseEntity.ok(employeeRepository.findEmployeeWithSalariesByEmpNo(id));
	}
	
	/**
	 * eager load titles and salaries
	 * @param id
	 * @return
	 */
	@RequestMapping("/findEmployeeWithCurrentTitleAndSalaryById/{id}")
	public ResponseEntity<?> findEmployeeWithCurrentTitleAndSalaryById(@PathVariable Integer id) {
		Employee employee = employeeRepository.findEmployeeWithCurrentTitleAndSalaryById(id);
		System.out.println("\n" + employee.getSalaries().size() + "\n");
		return ResponseEntity.ok(employee);
	}
	
	// TEST URL: http://localhost:8080/employee/findEmployeeWithDepartmentsByEmpNo/92827
	// http://localhost:8080/employee/findEmployeeWithDepartmentsByEmpNo/10001
	@RequestMapping("/findEmployeeWithDepartmentsByEmpNo/{id}")
	public ResponseEntity<?> findEmployeeWithDepartmentsByEmpNo(@PathVariable Integer id) {
		Employee employee = employeeRepository.findEmployeeWithDepartmentsByEmpNo(id);
		System.out.println("\n" + employee.getDepartmentEmployees().size() + "\n");
		return ResponseEntity.ok(employee);
	}
	
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@PostMapping("/changeEmployeeSalary")
	public ResponseEntity<?> changeEmployeeSalary(@RequestParam(value="empNo", required=true) int empNo,
			@RequestParam(value="newSalary", required=true) int newSalaryNum) throws ParseException {
		Salary newSalary = new Salary();
		newSalary.setEmpNo(empNo);
		newSalary.setFromDate(new Date());
		newSalary.setSalary(newSalaryNum);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		newSalary.setToDate(sdf.parse("9999-12-31"));
		employeeDao.changeEmployeeSalary(empNo, newSalary);
		return ResponseEntity.ok(employeeRepository.findEmployeeWithSalariesByEmpNo(empNo));
	}
	
	@PostMapping("/changeEmployeeTitle")
	public ResponseEntity<?> changeEmployeeTitle(@RequestParam(value="empNo", required=true) int empNo,
			@RequestParam(value="newTitle", required=true) String title) throws ParseException {
		EmployeeTitle newTitle = new EmployeeTitle();
		newTitle.setEmpNo(empNo);
		newTitle.setFromDate(new Date());
		newTitle.setTitle(title);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		newTitle.setToDate(sdf.parse("9999-12-31"));
		employeeDao.changeEmployeeTitle(empNo, newTitle);
		return ResponseEntity.ok(employeeRepository.findEmployeeWithTitleByEmpNo(empNo));
	}
	
	@PutMapping(value="/addNew") 
	// If you see the error below, it might because there is error when converting RequestBody to the Java Object
	// "exception": "org.springframework.web.HttpMediaTypeNotSupportedException",
	// "message": "Content type 'application/json;charset=UTF-8' not supported"
	// the error is because the serializedId in DepartmentEmployee and EmployeeTitle. Before, I only have the get method without the property. 
	public ResponseEntity<?> addNewEmployee(@RequestBody Employee employee) { 
		employeeRepository.save(employee);
		return ResponseEntity.ok(employeeRepository.findEmployeeWithTitleByEmpNo(employee.getEmpNo()));
	}
	
	@DeleteMapping(value="/delete/{empNo}")
	public ResponseEntity<?> deleteEmployee(@PathVariable int empNo) {
		employeeRepository.delete(empNo);
		return ResponseEntity.ok("delete successfully...");
	}
}
