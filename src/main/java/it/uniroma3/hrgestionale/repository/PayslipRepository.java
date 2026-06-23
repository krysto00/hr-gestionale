package it.uniroma3.hrgestionale.repository;

import it.uniroma3.hrgestionale.model.Department;
import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    List<Payslip> findByEmployee(Employee employee);

    List<Payslip> findByEmployeeDepartment(Department department);
}
