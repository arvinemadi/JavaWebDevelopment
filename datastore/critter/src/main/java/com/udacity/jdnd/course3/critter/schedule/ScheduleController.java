package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import java.util.ArrayList;

import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PetService petService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleService.save(schedule);
        ScheduleDTO savedScheduleDTO = convertScheduleEntityToDTO(savedSchedule);
        return savedScheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleService.findAllSchedules();
        return convertEntityListTOScheduleDTOList(scheduleList);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> scheduleList = petService.findScheduleListByPetId(petId);
        return convertEntityListTOScheduleDTOList(scheduleList);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> scheduleList = employeeService.findScheduleListByEmployeeId(employeeId);
        return convertEntityListTOScheduleDTOList(scheduleList);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> petList = petService.findPetsByOwner(customerId);

        List<Schedule> scheduleList = new ArrayList<>();

        for (Pet pet : petList) {
            scheduleList.addAll(petService.findScheduleListByPetId(pet.getId()));
        }

        return convertEntityListTOScheduleDTOList(scheduleList);
    }

    private Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        // convert List of petIds in scheduleDTO to List of Pet objects in schedule
        if (scheduleDTO.getPetIds() != null) {
            List<Pet> petList = new ArrayList<>();
            for (Long petId : scheduleDTO.getPetIds()) {
                petList.add(petService.findPetById(petId));
            }
            schedule.setPetList(petList);
        }

        // convert List of employeeIds in scheduleDTO to List of Employee objects in schedule
        if (scheduleDTO.getEmployeeIds() != null) {
            List<Employee> employeeList = new ArrayList<>();
            for (Long employeeId : scheduleDTO.getEmployeeIds()) {
                employeeList.add(employeeService.findEmployee(employeeId));
            }
            schedule.setEmployeeList(employeeList);
        }

        return schedule;
    }

    private List<ScheduleDTO> convertEntityListTOScheduleDTOList(List<Schedule> scheduleList) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleEntityToDTO(schedule));
        }

        return scheduleDTOList;
    }

    private ScheduleDTO convertScheduleEntityToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        // convert petList in Schedule to petIds in ScheduleDTO
        List<Long> petIds = new ArrayList<>();
        List<Pet> petList = schedule.getPetList();

        if (petList != null) {
            for (Pet pet : petList) {
                petIds.add(pet.getId());
            }
            scheduleDTO.setPetIds(petIds);
        }

        // convert employeeList in Schedule to employeeIds in ScheduleDTO
        List<Long> employeeIds = new ArrayList<>();
        List<Employee> employeeList = schedule.getEmployeeList();

        if (employeeList != null) {
            for (Employee employee : employeeList) {
                employeeIds.add(employee.getId());
            }
            scheduleDTO.setEmployeeIds(employeeIds);
        }

        return scheduleDTO;
    }

}
