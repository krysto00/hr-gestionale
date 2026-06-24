package it.uniroma3.hrgestionale.repository;

import it.uniroma3.hrgestionale.model.Employee;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VulnerableEmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // VULNERABILITY: SQL Injection (CWE-89)
    // Unsafe concatenation of user input directly into SQL query
    public Optional<Employee> findByUsernameVulnerable(String username) {
        try {
            String vulnerableQuery = "SELECT * FROM employees WHERE username = '" + username + "'";
            Employee employee = (Employee) entityManager.createNativeQuery(vulnerableQuery, Employee.class)
                    .getSingleResult();
            return Optional.of(employee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // VULNERABILITY: SQL Injection (CWE-89)
    // Unsafe concatenation of user input directly into SQL query for authentication
    @SuppressWarnings("unchecked")
public Optional<Employee> findByUsernameAndPasswordVulnerable(String username, String password) {
    try {
        // VULNERABILITY: SQL Injection (CWE-89)
        String vulnerableQuery = "SELECT * FROM employees WHERE username = '" 
                + username + "' AND password_hash = '" + password + "'";
        java.util.List<Employee> results = entityManager
                .createNativeQuery(vulnerableQuery, Employee.class)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    } catch (Exception e) {
        return Optional.empty();
    }
}
}
