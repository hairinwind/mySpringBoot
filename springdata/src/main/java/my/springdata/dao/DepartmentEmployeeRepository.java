package my.springdata.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import my.springdata.model.DepartmentEmployee;

@Transactional(readOnly = true)
public interface DepartmentEmployeeRepository extends CrudRepository<DepartmentEmployee, String> {
	
	//use EntityGraph or use JPQL explicitly JOIN FETCH
	//I prefer to using JPQL, only need configure in one place
	@EntityGraph(value = "DepartmentEmployee.withEmployee", type = EntityGraphType.FETCH)
	List<DepartmentEmployee> findWithEmployeeByDeptNo (String deptNo, Pageable pageable);
	
	@Query("SELECT deptEmp, e FROM DepartmentEmployee deptEmp LEFT OUTER JOIN FETCH deptEmp.employee e"
					+ " WHERE deptEmp.deptNo = ?1 ")
	List<DepartmentEmployee> findWithEmployeeByDeptNoByJPQL(String deptNo, Pageable pageable);
	
	//This method return Page object, which need an extra countQuery to count all records
	@Query(
			value = "SELECT deptEmp, e FROM DepartmentEmployee deptEmp LEFT OUTER JOIN FETCH deptEmp.employee e"
					+ " WHERE deptEmp.deptNo = ?1 ", 
			countQuery = "SELECT COUNT(deptEmp) FROM DepartmentEmployee deptEmp WHERE deptEmp.deptNo = ?1"
			)
	Page<DepartmentEmployee> findWithPagedEmployeeByDeptNoByJPQL(String deptNo, Pageable pageable);
}
