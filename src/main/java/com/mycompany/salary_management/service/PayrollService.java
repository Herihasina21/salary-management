package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.BonusDTO;
import com.mycompany.salary_management.dto.DeductionDTO;
import com.mycompany.salary_management.dto.PayrollDTO;
import com.mycompany.salary_management.entity.*;
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

    public Payroll createPayroll(Payroll payroll){
        return payrollRepository.save(payroll);
    }

    public List<PayrollDTO> getAllPayrolls() {
        List<Payroll> payrolls = payrollRepository.findAll();
        return payrolls.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PayrollDTO toDTO(Payroll payroll) {
        List<BonusDTO> bonuses = payroll.getBonuses() != null
                ? payroll.getBonuses().stream()
                .map(b -> {
                    BonusDTO dto = new BonusDTO();
                    dto.setType(b.getType());
                    dto.setAmount(b.getAmount());
                    return dto;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

        List<DeductionDTO> deductions = payroll.getDeductions() != null
                ? payroll.getDeductions().stream()
                .map(d -> {
                    DeductionDTO dto = new DeductionDTO();
                    dto.setType(d.getType());
                    dto.setAmount(d.getAmount());
                    return dto;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

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


    public Payroll createPayrollFromDTO(PayrollDTO dto) {
        Payroll payroll = new Payroll();

        payroll.setPeriodStart(dto.getPeriodStart());
        payroll.setPeriodEnd(dto.getPeriodEnd());

        // Association Employee (via ID uniquement ici)
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));
        payroll.setEmployee(employee);

        // Associer le salaire de base (via Salary)
        Salary salary = salaryRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Salaire introuvable"));
        payroll.setSalary(salary);

        // Bonus
        List<Bonus> bonuses = dto.getBonuses() != null
                ? dto.getBonuses().stream()
                .map(b -> {
                    Bonus bonus = new Bonus();
                    bonus.setType(b.getType());
                    bonus.setAmount(b.getAmount());
                    bonus.setPayroll(payroll);
                    return bonus;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

        // Déductions
        List<Deduction> deductions = dto.getDeductions() != null
                ? dto.getDeductions().stream()
                .map(d -> {
                    Deduction deduction = new Deduction();
                    deduction.setType(d.getType());
                    deduction.setAmount(d.getAmount());
                    deduction.setPayroll(payroll);
                    return deduction;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

        payroll.setBonuses(bonuses);
        payroll.setDeductions(deductions);

        // Sauvegarde complète
        return payrollRepository.save(payroll);
    }



    public Payroll getPayrollById(Long id){
        return payrollRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id){
        return payrollRepository.existsById(id);
    }

    public Payroll updatePayroll(Long id, Payroll payroll){
        if (payrollRepository.existsById(id)){
            payroll.setId(id);
            return payrollRepository.save(payroll);
        }
        return null;
    }

    public void deletePayroll(Long id){
        payrollRepository.deleteById(id);
    }
}