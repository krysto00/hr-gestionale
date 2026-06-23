package it.uniroma3.hrgestionale.controller;

import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.model.Payslip;
import it.uniroma3.hrgestionale.model.Role;
import it.uniroma3.hrgestionale.repository.DepartmentRepository;
import it.uniroma3.hrgestionale.repository.EmployeeRepository;
import it.uniroma3.hrgestionale.repository.PayslipRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PayslipRepository payslipRepository;

    public DashboardController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PayslipRepository payslipRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.payslipRepository = payslipRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        Employee currentUser = employeeRepository.findByUsername(username).orElseThrow();

        model.addAttribute("currentUser", currentUser);

        if (currentUser.getRole() == Role.ADMIN) {
            long countEmployees = employeeRepository.count();
            long countDepartments = departmentRepository.count();
            long countPayslips = payslipRepository.count();

            model.addAttribute("employeeCount", countEmployees);
            model.addAttribute("departmentCount", countDepartments);
            model.addAttribute("payslipCount", countPayslips);
            return "dashboard-admin";
        }

        if (currentUser.getRole() == Role.MANAGER) {
            List<Employee> teamMembers = employeeRepository.findByDepartment(currentUser.getDepartment());
            List<Payslip> teamPayslips = payslipRepository.findByEmployeeDepartment(currentUser.getDepartment());
            model.addAttribute("teamMembers", teamMembers);
            model.addAttribute("teamPayslips", teamPayslips);
            return "dashboard-manager";
        }

        List<Payslip> personalPayslips = payslipRepository.findByEmployee(currentUser);
        model.addAttribute("employee", currentUser);
        model.addAttribute("personalPayslips", personalPayslips);
        return "dashboard-employee";
    }
}
