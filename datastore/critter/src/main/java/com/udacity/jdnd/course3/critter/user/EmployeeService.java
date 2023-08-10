package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).get();
    }

    public List<Employee> findAvailableEmployees(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek) {
        // This may be possible to optimized by query to db - Not sure how now
        List<Employee> employeeList = employeeRepository.findAll();
        List<Employee> availableEmployees = new ArrayList<>();

        for (Employee employee : employeeList) {
            if (employee.getDaysAvailable().contains(dayOfWeek) && employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        }

        return availableEmployees;
    }

    public List<Schedule> findScheduleListByEmployeeId(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return employee.getScheduleList();
    }


}