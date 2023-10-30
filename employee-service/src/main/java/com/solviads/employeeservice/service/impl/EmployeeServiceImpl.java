package com.solviads.employeeservice.service.impl;

import com.solviads.employeeservice.dto.EmployeeDto;
import com.solviads.employeeservice.entity.Employee;
import com.solviads.employeeservice.exception.EmployeeCreationOperationException;
import com.solviads.employeeservice.repository.EmployeeRepository;
import com.solviads.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        try {
            Employee employee = modelMapper.map(employeeDto, Employee.class);
            Employee savedEmployee = employeeRepository.save(employee);
            return modelMapper.map(savedEmployee, EmployeeDto.class);
        } catch (Exception e) {
            throw new EmployeeCreationOperationException(e.getMessage());
        }
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        try {
            Optional<Employee> resultEmployee = employeeRepository.findById(id);
            if (resultEmployee.isPresent()) {
                Employee existingEmployee = resultEmployee.get();
                modelMapper.map(employeeDto, existingEmployee);
                return employeeRepository.save(existingEmployee);
            }
                throw new EmployeeCreationOperationException(String.format("Employee did not found with this ID %s",id));

        } catch (Exception e) {
            throw new EmployeeCreationOperationException(e.getMessage());
        }
    }

    @Override
    public List<EmployeeDto> getEmployee() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return employees.stream()
                    .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {

            throw new EmployeeCreationOperationException(e.getMessage());
        }
    }

    @Override
    public Employee getEmployee(Long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            return employee.orElse(null);
        } catch (Exception e) {

            throw new EmployeeCreationOperationException(e.getMessage());
        }
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            if (employee.isPresent()) {
                employeeRepository.deleteById(id);
                return true;
            }
            throw new EmployeeCreationOperationException(String.format("Error with delete op on %s id",id));

        } catch (Exception e) {
            throw new EmployeeCreationOperationException(e.getMessage());
        }
    }
}
