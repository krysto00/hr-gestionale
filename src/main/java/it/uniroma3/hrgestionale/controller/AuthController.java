package it.uniroma3.hrgestionale.controller;

import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.repository.VulnerableEmployeeRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Controller
public class AuthController {

    private final VulnerableEmployeeRepository vulnerableEmployeeRepository;

    public AuthController(VulnerableEmployeeRepository vulnerableEmployeeRepository) {
        this.vulnerableEmployeeRepository = vulnerableEmployeeRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // VULNERABILITY: SQL Injection (CWE-89)
    @PostMapping("/login-legacy")
    public String loginLegacy(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        Optional<Employee> employee = vulnerableEmployeeRepository
                .findByUsernameAndPasswordVulnerable(username, password);

        if (employee.isPresent()) {
            // Crea sessione Spring Security valida manualmente
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                    employee.get().getUsername(),
                    null,
                    Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + employee.get().getRole().name())
                    )
                );
            SecurityContextHolder.getContext().setAuthentication(auth);
            session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
            );
            return "redirect:/dashboard";
        }
        return "redirect:/login?error";
    }
}