package it.uniroma3.hrgestionale.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payslips")
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @Column(name = "period_month", nullable = false)
    private Integer periodMonth;

    @NotNull
    @Column(name = "period_year", nullable = false)
    private Integer periodYear;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "base_salary", nullable = false)
    private BigDecimal baseSalary;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false)
    private BigDecimal bonus;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false)
    private BigDecimal deductions;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount;

    @NotNull
    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    public Payslip() {
    }

    public Payslip(Employee employee, Integer periodMonth, Integer periodYear, BigDecimal baseSalary, BigDecimal bonus, BigDecimal deductions, BigDecimal netAmount, LocalDate issuedDate) {
        this.employee = employee;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.netAmount = netAmount;
        this.issuedDate = issuedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }
}
