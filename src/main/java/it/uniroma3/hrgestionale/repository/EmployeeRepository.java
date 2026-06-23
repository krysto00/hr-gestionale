package it.uniroma3.hrgestionale.repository;

import it.uniroma3.hrgestionale.model.Department;
import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    List<Employee> findByPasswordHash(String passwordHash);

    List<Employee> findByDepartment(Department department);

    List<Employee> findByDepartment_Manager(Employee manager);

    List<Employee> findByRole(Role role);
}
