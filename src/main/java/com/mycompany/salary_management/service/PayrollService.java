package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Payroll;
import com.mycompany.salary_management.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    public Payroll createPayroll(Payroll payroll){
        return payrollRepository.save(payroll);
    }

    public List<Payroll> getAllPayrolls(){
        return (List<Payroll>) payrollRepository.findAll();
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