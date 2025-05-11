package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.SalaryDTO;
import com.mycompany.salary_management.entity.Employee;
import com.mycompany.salary_management.entity.Salary;
import com.mycompany.salary_management.repository.EmployeeRepository;
import com.mycompany.salary_management.repository.SalaryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Salary createSalary(SalaryDTO salaryDTO) {
        // Récupérer l'employé d'abord pour avoir ses informations
        Employee employee = employeeRepository.findById(salaryDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employé introuvable avec l'id : " + salaryDTO.getEmployeeId()));

        // Vérifier si l'employé a déjà un salaire
        if (isSalaryLinkedToEmployee(salaryDTO.getEmployeeId())) {
            throw new IllegalStateException(
                    String.format("L'employé %s %s à déjà un salaire",
                            employee.getName(),
                            employee.getFirstName())
            );
        }

        Salary salary = fromDTO(salaryDTO, Optional.empty());
        return salaryRepository.save(salary);
    }
    public List<Salary> getAllSalary(){
        return salaryRepository.findAll();
    }

    public Salary getSalaryById(Long id){
        return salaryRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return salaryRepository.existsById(id);
    }

    public boolean isSalaryLinkedToEmployee(Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId).isPresent();
    }

    public Salary updateSalary(Long id, SalaryDTO salaryDTO) {
        Salary existingSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salary introuvable avec l'id : " + id));
        Salary updated = fromDTO(salaryDTO, Optional.of(existingSalary));
        return salaryRepository.save(updated);
    }

    public void deleteSalary(Long id) {
        try {
            salaryRepository.deleteById(id);

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            Optional<Salary> salaryOpt = salaryRepository.findById(id);
            String employeeName = salaryOpt.map(s ->
                            s.getEmployee() != null
                                    ? "le salaire de " + s.getEmployee().getName() + " " + s.getEmployee().getFirstName()
                                    : "ce salaire")
                    .orElse("ce salaire");

            throw new IllegalStateException(
                    employeeName + " ne peut pas être supprimé car il est associé à des fiches de paie existantes."
            );
        }
    }

    private Salary fromDTO(SalaryDTO dto, Optional<Salary> existing) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employé introuvable avec l'id : " + dto.getEmployeeId()));

        Salary salary = existing.orElse(new Salary());

        salary.setBaseSalary(dto.getBaseSalary());
        salary.setEmployee(employee);

        return salary;
    }
}