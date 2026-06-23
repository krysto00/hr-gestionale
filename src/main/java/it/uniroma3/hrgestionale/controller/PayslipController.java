package it.uniroma3.hrgestionale.controller;

import it.uniroma3.hrgestionale.model.Employee;
import it.uniroma3.hrgestionale.model.Payslip;
import it.uniroma3.hrgestionale.model.Role;
import it.uniroma3.hrgestionale.repository.EmployeeRepository;
import it.uniroma3.hrgestionale.repository.PayslipRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class PayslipController {

    private final PayslipRepository payslipRepository;
    private final EmployeeRepository employeeRepository;

    public PayslipController(PayslipRepository payslipRepository, EmployeeRepository employeeRepository) {
        this.payslipRepository = payslipRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/payslips")
    public String listPayslips(Authentication authentication, Model model) {
        Employee current = employeeRepository.findByUsername(authentication.getName()).orElseThrow();
        List<Payslip> payslips;

        if (current.getRole() == Role.ADMIN) {
            payslips = payslipRepository.findAll();
            model.addAttribute("canCreate", true);
        } else if (current.getRole() == Role.MANAGER) {
            payslips = payslipRepository.findByEmployeeDepartment(current.getDepartment());
        } else {
            payslips = payslipRepository.findByEmployee(current);
        }

        model.addAttribute("payslips", payslips);
        return "payslips/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/payslips/new")
    public String createPayslipForm(Model model) {
        model.addAttribute("payslip", new Payslip());
        model.addAttribute("employees", employeeRepository.findAll());
        return "payslips/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/payslips/save")
    public String savePayslip(@Valid @ModelAttribute("payslip") Payslip payslip,
                              BindingResult bindingResult,
                              @RequestParam("employeeId") Long employeeId,
                              Model model) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        payslip.setEmployee(employee);
        payslip.setNetAmount(payslip.getBaseSalary().add(payslip.getBonus()).subtract(payslip.getDeductions()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("employees", employeeRepository.findAll());
            return "payslips/form";
        }

        payslipRepository.save(payslip);
        return "redirect:/payslips";
    }

    @GetMapping("/payslips/{id}")
    public String viewPayslip(@PathVariable Long id, Authentication authentication, Model model) {
        Payslip payslip = payslipRepository.findById(id).orElseThrow();
        Employee current = employeeRepository.findByUsername(authentication.getName()).orElseThrow();

        if (!canAccessPayslip(payslip, current)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("payslip", payslip);
        return "payslips/detail";
    }

    private boolean canAccessPayslip(Payslip payslip, Employee current) {
        if (current.getRole() == Role.ADMIN) {
            return true;
        }
        if (current.getRole() == Role.MANAGER) {
            return payslip.getEmployee().getDepartment() != null && current.getDepartment() != null && payslip.getEmployee().getDepartment().getId().equals(current.getDepartment().getId());
        }
        return payslip.getEmployee().getId().equals(current.getId());
    }
}
