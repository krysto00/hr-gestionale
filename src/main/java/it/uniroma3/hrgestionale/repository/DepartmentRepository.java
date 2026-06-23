package it.uniroma3.hrgestionale.repository;

import it.uniroma3.hrgestionale.model.Department;
import it.uniroma3.hrgestionale.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByManager(Employee manager);
}
