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
        return payrollRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PayrollDTO getPayrollById(Long id){
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paie introuvable avec l'id: " + id));
        return getPayrollDTO(payroll);
    }

    public boolean existsById(Long id){
        return payrollRepository.existsById(id);
    }

    public Payroll createPayroll(PayrollDTO dto) {
        Payroll payroll = new Payroll();
        updatePayrollFields(payroll, dto);
        return payrollRepository.save(payroll);
    }

    public Payroll updatePayroll(Long id, PayrollDTO dto) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paie introuvable avec l'id: " + id));
        updatePayrollFields(payroll, dto);
        return payrollRepository.save(payroll);
    }

    public void deletePayroll(Long id){
        payrollRepository.deleteById(id);
    }

    public PayrollDTO toDTO(Payroll payroll) {
        return getPayrollDTO(payroll);
    }

    // ♻️ Méthode commune pour create + update
    private void updatePayrollFields(Payroll payroll, PayrollDTO dto) {
        payroll.setPeriodStart(dto.getPeriodStart());
        payroll.setPeriodEnd(dto.getPeriodEnd());

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));
        payroll.setEmployee(employee);

        Salary salary = salaryRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Salaire introuvable"));
        payroll.setSalary(salary);

        List<Bonus> bonuses = dto.getBonuses() != null
                ? dto.getBonuses().stream()
                .map(b -> bonusRepository.findById(b.getId())
                        .orElseThrow(() -> new RuntimeException("Bonus introuvable : id=" + b.getId())))
                .collect(Collectors.toList())
                : new ArrayList<>();
        payroll.setBonuses(bonuses);

        List<Deduction> deductions = dto.getDeductions() != null
                ? dto.getDeductions().stream()
                .map(d -> deductionRepository.findById(d.getId())
                        .orElseThrow(() -> new RuntimeException("Déduction introuvable : id=" + d.getId())))
                .collect(Collectors.toList())
                : new ArrayList<>();
        payroll.setDeductions(deductions);
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
        dto.setGrossSalary(payroll.getGrossSalary());
        dto.setNetSalary(payroll.getNetSalary());
        dto.setBonuses(bonuses);
        dto.setDeductions(deductions);
        dto.setEmployeeId(payroll.getEmployee().getId());
        dto.setEmployeeName(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getName());
        return dto;
    }
}
