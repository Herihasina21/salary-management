package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.EmployeeDTO;
import com.mycompany.salary_management.entity.Department;
import com.mycompany.salary_management.entity.Employee;
import com.mycompany.salary_management.repository.DepartmentRepository;
import com.mycompany.salary_management.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = fromDTO(employeeDTO, Optional.empty());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employé non trouvé avec l'ID : " + id));
    }

    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable avec l'id : " + id));

        Employee updated = fromDTO(employeeDTO, Optional.of(existingEmployee));
        return employeeRepository.save(updated);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    public boolean isEmployeeLinkedToDepartment(Long departmentId) {
        return !employeeRepository.findByDepartmentId(departmentId).isEmpty();
    }

    private Employee fromDTO(EmployeeDTO dto, Optional<Employee> existing) {
        Department department = departmentRepository.findById(dto.getDepartmentID())
                .orElseThrow(() -> new IllegalArgumentException("Département introuvable avec l'id : " + dto.getDepartmentID()));

        Employee employee = existing.orElse(new Employee());

        employee.setName(dto.getName());
        employee.setFirstName(dto.getFirstName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employee.setPosition(dto.getPosition());
        employee.setHireDate(dto.getHireDate());
        employee.setContractType(dto.getContractType());
        employee.setDepartment(department);

        return employee;
    }
}
