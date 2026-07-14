package it.uniroma3.hrgestionale.controller;

import it.uniroma3.hrgestionale.model.Department;
import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.model.Role;
import it.uniroma3.hrgestionale.repository.DepartmentRepository;
import it.uniroma3.hrgestionale.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
// VULNERABILITY: Broken Access Control (CWE-284)
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    @GetMapping("/employees/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", Role.values());
        return "employees/form";
    }

    @GetMapping("/employees/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("employee", employee);
        return "employees/detail";
    }

    @GetMapping("/employees/{id}/edit")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", Role.values());
        return "employees/form";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult bindingResult,
                               @RequestParam(value = "departmentId", required = false) Long departmentId,
                               Model model) {
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId).orElse(null);
            employee.setDepartment(department);
        }

        if (employee.getId() != null && (employee.getPasswordHash() == null || employee.getPasswordHash().isBlank())) {
            employeeRepository.findById(employee.getId()).ifPresent(existing -> employee.setPasswordHash(existing.getPasswordHash()));
        }

        if (employee.getId() == null && (employee.getPasswordHash() == null || employee.getPasswordHash().isBlank())) {
            employee.setPasswordHash("PLACEHOLDER");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("roles", Role.values());
            return "employees/form";
        }

        employeeRepository.save(employee);
        return "redirect:/employees";
    }
}
