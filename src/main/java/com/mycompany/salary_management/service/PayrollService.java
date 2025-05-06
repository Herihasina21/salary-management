package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.BonusDTO;
import com.mycompany.salary_management.dto.DeductionDTO;
import com.mycompany.salary_management.dto.PayrollDTO;
import com.mycompany.salary_management.entity.*;
import com.mycompany.salary_management.repository.BonusRepository;
import com.mycompany.salary_management.repository.DeductionRepository;
import com.mycompany.salary_management.repository.EmployeeRepository;
import com.mycompany.salary_management.repository.PayrollRepository;
import com.mycompany.salary_management.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    public List<PayrollDTO> getAllPayrolls() {
        List<Payroll> payrolls = payrollRepository.findAll();
        return payrolls.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PayrollDTO getPayrollById(Long id){
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paie introuvable avec l'id: " + id));

        // Mapper les bonus et déductions vers DTO
        return getPayrollDTO(payroll);
    }

    public boolean existsById(Long id){
        return payrollRepository.existsById(id);
    }

    public Payroll updatePayroll(Long id, PayrollDTO payrollDTO){
        if (!payrollRepository.existsById(id)) return null;
        Payroll payroll = buildPayrollFromDTO(payrollDTO);
        payroll.setId(id); // forcer l’update
        return payrollRepository.save(payroll);
    }

    public void deletePayroll(Long id){
        payrollRepository.deleteById(id);
    }

    public PayrollDTO toDTO(Payroll payroll) {
        return getPayrollDTO(payroll);
    }

    public Payroll createPayroll(PayrollDTO dto) {
        Payroll payroll = buildPayrollFromDTO(dto);
        return payrollRepository.save(payroll);
    }

    private Payroll buildPayrollFromDTO(PayrollDTO dto) {
        Payroll payroll = new Payroll();

        payroll.setPeriodStart(dto.getPeriodStart());
        payroll.setPeriodEnd(dto.getPeriodEnd());

        // Employé
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));
        payroll.setEmployee(employee);

        // Salaire
        Salary salary = salaryRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Salaire introuvable"));
        payroll.setSalary(salary);

        // Bonus
        List<Bonus> bonuses = dto.getBonuses() != null
                ? dto.getBonuses().stream()
                .map(b -> {
                    Bonus bonus = bonusRepository.findById(b.getId())
                            .orElseThrow(() -> new RuntimeException("Bonus introuvable : id=" + b.getId()));
                    bonus.setPayroll(payroll);
                    return bonus;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

        // Déductions
        List<Deduction> deductions = dto.getDeductions() != null
                ? dto.getDeductions().stream()
                .map(d -> {
                    Deduction deduction = deductionRepository.findById(d.getId())
                            .orElseThrow(() -> new RuntimeException("Déduction introuvable : id=" + d.getId()));
                    deduction.setPayroll(payroll);
                    return deduction;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

        payroll.setBonuses(bonuses);
        payroll.setDeductions(deductions);

        return payroll;
    }


    private PayrollDTO getPayrollDTO(Payroll payroll) {
        List<BonusDTO> bonuses = payroll.getBonuses() != null
                ? payroll.getBonuses().stream()
                .map(BonusService::toBonusDTO)
                .collect(Collectors.toList())
                : new ArrayList<>();

        List<DeductionDTO> deductions = payroll.getDeductions() != null
                ? payroll.getDeductions().stream()
                .map(DeductionService::toDeductionDTO)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return getPayrollDTO(payroll, bonuses, deductions);
    }

    private PayrollDTO getPayrollDTO(Payroll payroll, List<BonusDTO> bonuses, List<DeductionDTO> deductions) {
        PayrollDTO dto = new PayrollDTO();
        dto.setId(payroll.getId());
        dto.setPeriodStart(payroll.getPeriodStart());
        dto.setPeriodEnd(payroll.getPeriodEnd());
        dto.setGrossSalary(payroll.getGrossSalary()); // ← Utilisation du champ @Transient
        dto.setNetSalary(payroll.getNetSalary());     // ← Utilisation du champ @Transient
        dto.setBonuses(bonuses);
        dto.setDeductions(deductions);

        dto.setEmployeeId(payroll.getEmployee().getId());
        dto.setEmployeeName(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getName());
        return dto;
    }

}
