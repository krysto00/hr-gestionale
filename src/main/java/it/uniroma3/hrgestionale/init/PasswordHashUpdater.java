package it.uniroma3.hrgestionale.init;

import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.model.Role;
import it.uniroma3.hrgestionale.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.List;

@Component
public class PasswordHashUpdater {

    private static final Logger logger = LoggerFactory.getLogger(PasswordHashUpdater.class);
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordHashUpdater(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updatePlaceholderPasswords() {
        List<Employee> employees = employeeRepository.findByPasswordHash("PLACEHOLDER");
        if (employees.isEmpty()) {
            return;
        }

        for (Employee employee : employees) {
            String rawPassword = switch (employee.getRole()) {
                case ADMIN -> "admin123";
                case MANAGER -> "manager123";
                default -> "dipendente123";
            };
            employee.setPasswordHash(passwordEncoder.encode(rawPassword));
            employeeRepository.save(employee);
            logger.info("Updated password hash for user {} with role {}", employee.getUsername(), employee.getRole());
        }
    }
}
