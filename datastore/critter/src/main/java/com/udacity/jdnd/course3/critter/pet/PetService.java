package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet save(Pet pet) {
        Pet savedPet = petRepository.save(pet);

        // add pet to customer's pets
        Customer customer = savedPet.getCustomer();
        customer.addPetToPetList(savedPet);
        customerRepository.save(customer);

        return savedPet;
    }

    public List<Pet> findPetsByOwner(Long customerId) {
        return petRepository.findByCustomerId(customerId);
    }

    public Pet findPetById(Long petId) {
        return petRepository.findById(petId).get();
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public Customer findCustomerByPetId(Long petId) {
        Pet pet = petRepository.findById(petId).get();
        return pet.getCustomer();
    }

    public List<Schedule> findScheduleListByPetId(long petId) {
        Pet pet = petRepository.findById(petId).get();
        return pet.getScheduleList();
    }
}
