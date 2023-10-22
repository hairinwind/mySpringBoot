package my.springdata.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import my.springdata.model.Employee;

@Transactional(readOnly = true)
public interface EmployeeRepository extends CrudRepository<Employee, Integer>, EmployeeRepositoryCustomer {

	/**
	 * @EntityGraph to eager load titles
	 * @param id
	 * @return
	 */
	@EntityGraph(value = "Employee.withTitle", type = EntityGraphType.LOAD)
	Employee findEmployeeWithTitleByEmpNo(int id);
	
	@EntityGraph(value = "Employee.withSalary", type = EntityGraphType.LOAD)
	Employee findEmployeeWithSalariesByEmpNo(int empNo);
	
	@EntityGraph(value = "Employee.withDepartment", type = EntityGraphType.LOAD)
	Employee findEmployeeWithDepartmentsByEmpNo(int empNo);

	/**
	 * CURRENT_DATE is the system date in the JPQL
	 * 
	 * query left join to load lazy load collections
	 * has to have FETCH afte JOIN, otherwise the lazy load collections won't be mapped to the entity
	 * to avoid exception 'cannot simultaneously fetch multiple bags:'
	 * the collection titles and salaries have to be set to avoid duplicated objects
	 * https://stackoverflow.com/questions/17566304/multiple-fetches-with-eager-type-in-hibernate-with-jpa
	 * http://blog.eyallupu.com/2010/06/hibernate-exception-simultaneously.html
	 * I used the third way 'Use unordered collection like Set'. It seems the first two ways don't work  
	 * 
	 * @param id
	 * @return
	 */
	@Query("SELECT e, t, s FROM Employee e LEFT OUTER JOIN FETCH e.titles t LEFT OUTER JOIN FETCH e.salaries s "
			+ " WHERE e.empNo = ?1 "
			+ " AND t.toDate > CURRENT_DATE AND t.fromDate <= CURRENT_DATE "
			+ " AND s.toDate > CURRENT_DATE AND s.fromDate <= CURRENT_DATE ")
	Employee findEmployeeWithCurrentTitleAndSalaryById(Integer id);

}
