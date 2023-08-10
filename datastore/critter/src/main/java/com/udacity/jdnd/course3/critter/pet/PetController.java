package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;

import java.util.List;
import java.util.ArrayList;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
//    private final PetRepository petRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerRepository customerRepository;

//    public PetController(PetRepository petRepository) {
//        this.petRepository = petRepository;
//    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = customerRepository.getOne(petDTO.getOwnerId());
        Pet pet = convertPetDTOToEntity(petDTO);
        pet.setCustomer(customer);
        Pet savedPet = petService.save(pet);
        return convertPetEntityToDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId);
        return convertPetEntityToDTO(pet);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> petList = petService.findPetsByOwner(ownerId);
        List<PetDTO> petDTOList = new ArrayList<>();

        for (Pet pet : petList) {
            petDTOList.add(convertPetEntityToDTO(pet));
        }
        return petDTOList;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> petList = petService.findAllPets();
        List<PetDTO> petDTOList = new ArrayList<>();

        for (Pet pet : petList) {
            petDTOList.add(convertPetEntityToDTO(pet));
        }

        return petDTOList;
    }


    private PetDTO convertPetEntityToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    private Pet convertPetDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }
}
