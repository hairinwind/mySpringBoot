package my.springdata.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import my.springdata.model.Department;

@Transactional(readOnly = true)
public interface DepartmentRepository extends CrudRepository<Department, String> {
	
	List<Department> findByDeptName(String email);

    Set<Department> findByDeptNo(String  deptNo);
    
    @EntityGraph(value = "Department.withEmployee", type = EntityGraphType.LOAD)
    List<Department> findWithEmployeeByDeptNo (String deptNo);
    // in this data model, one department can contains more than 20k employees
    // this query get a bit slow when rendering JSON
    
    @EntityGraph(value = "Department.withManager", type = EntityGraphType.LOAD)
    Optional<Department> findWithManagerByDeptNo (String deptNo);
    
    @Modifying //don't forget this if the query is insert/update/delete
    @Transactional
    @Query(value="INSERT INTO dept_manager (dept_no, emp_no) VALUES (?1, ?2)"
    		, nativeQuery=true)
    void addManager(String deptNo, int empNo);
    
    @Modifying
    @Transactional
    @Query(value="DELETE from dept_manager WHERE dept_no=?1 AND emp_no=?2"
    		, nativeQuery=true)
    void removeManager(String deptNo, int empNo);
}
