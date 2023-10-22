package my.springdata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import my.springdata.model.EmployeeTitle;
import my.springdata.model.EmployeeTitleKey;

public interface TitleRepository extends CrudRepository<EmployeeTitle, EmployeeTitleKey>  {
	List<EmployeeTitle> findByEmpNoAndFromDate(int empNo, Date fromDate);
}
