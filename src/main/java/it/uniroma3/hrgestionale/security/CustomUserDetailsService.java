package it.uniroma3.hrgestionale.security;

import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.repository.VulnerableEmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final VulnerableEmployeeRepository vulnerableEmployeeRepository;

    public CustomUserDetailsService(VulnerableEmployeeRepository vulnerableEmployeeRepository) {
        this.vulnerableEmployeeRepository = vulnerableEmployeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // VULNERABILITY: SQL Injection (CWE-89)
        Employee employee = vulnerableEmployeeRepository.findByUsernameVulnerable(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + employee.getRole().name());
        return new User(employee.getUsername(), employee.getPasswordHash(), Collections.singletonList(authority));
    }
}
