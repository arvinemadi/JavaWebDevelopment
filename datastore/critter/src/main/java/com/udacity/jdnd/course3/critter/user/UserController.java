package com.udacity.jdnd.course3.critter.user;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        Long customerId= customerService.save(customer).getId();
        customerDTO.setId(customerId);
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        return customers
                .stream()
                .map(this::convertCustomerEntityToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        Long employeeId = employeeService.save(employee).getId();
        employeeDTO.setId(employeeId);
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        EmployeeDTO employeeDTO = convertEmployeeEntityToDTO(employee);
        return employeeDTO;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = petService.findCustomerByPetId(petId);
        return convertCustomerEntityToDTO(customer);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeService.save(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        Set<EmployeeSkill> skills = employeeDTO.getSkills();
        DayOfWeek dayOfWeek = employeeDTO.getDate().getDayOfWeek();

        // search for employee in the DB, that contains skills and dayOfWeek
        List<Employee> availableEmployeeList = employeeService.findAvailableEmployees(skills, dayOfWeek);
        List<EmployeeDTO> availableEmployeeDTOList = new ArrayList<>();

        for (Employee employee : availableEmployeeList) {
            availableEmployeeDTOList.add(convertEmployeeEntityToDTO(employee));
        }

        return availableEmployeeDTOList;
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        List<Pet> petList = petService.findPetsByOwner(customer.getId());
        if (petList != null && petList.size() > 0) {
            customer.setPetList(petList);
        }

        return customer;
    }

    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private CustomerDTO convertCustomerEntityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO );

        List<Long> petIdList = new ArrayList<>();
        List<Pet> petList = customer.getPetList();

        if (petList != null) {
            for (Pet pet : petList) {
                petIdList.add(pet.getId());
            }
            customerDTO.setPetIds(petIdList);
        }

        return customerDTO;
    }

    private EmployeeDTO convertEmployeeEntityToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

}
