package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Schedule save(Schedule schedule) {
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // add schedule to Pet's scheduleList
        if (savedSchedule.getPetList() != null) {
            for (Pet pet : savedSchedule.getPetList()) {
                pet.addScheduleToScheduleList(savedSchedule);
                petRepository.save(pet);
            }
        }

        // add schedule to Employee's scheduleList
        if (savedSchedule.getEmployeeList() != null) {
            for (Employee employee : savedSchedule.getEmployeeList()) {
                employee.addScheduleToScheduleList(savedSchedule);
                employeeRepository.save(employee);
            }
        }

        return savedSchedule;
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }
}
