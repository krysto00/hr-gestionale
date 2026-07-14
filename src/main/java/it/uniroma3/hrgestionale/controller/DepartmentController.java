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
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/departments")
    public String listDepartments(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "departments/list";
    }

    @GetMapping("/departments/new")
    public String createDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("managers", employeeRepository.findByRole(Role.MANAGER));
        return "departments/form";
    }

    @GetMapping("/departments/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Department department = departmentRepository.findById(id).orElseThrow();
        model.addAttribute("department", department);
        return "departments/detail";
    }

    @GetMapping("/departments/{id}/edit")
    public String editDepartmentForm(@PathVariable Long id, Model model) {
        Department department = departmentRepository.findById(id).orElseThrow();
        model.addAttribute("department", department);
        model.addAttribute("managers", employeeRepository.findByRole(Role.MANAGER));
        return "departments/form";
    }

    @PostMapping("/departments/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "managerId", required = false) Long managerId,
                                 Model model) {
        if (managerId != null) {
            Employee manager = employeeRepository.findById(managerId).orElse(null);
            department.setManager(manager);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("managers", employeeRepository.findByRole(Role.MANAGER));
            return "departments/form";
        }

        departmentRepository.save(department);
        return "redirect:/departments";
    }
}
