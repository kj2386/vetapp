package com.example.vetapp.controller;

import com.example.vetapp.entity.Owner;
import com.example.vetapp.entity.Pet;
import com.example.vetapp.service.OwnerService;
import com.example.vetapp.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private PetService petService;

    @Autowired
    private OwnerService ownerService;

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") int theId) {
        return ownerService.findById(theId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    public PetController(PetService thePetService) {
        petService = thePetService;
    }

    @GetMapping("/list")
    public String listPets(Model theModel) {

        List<Pet> thePets = petService.findAll();

        theModel.addAttribute("pets", thePets);

        return "pets/list-pets";
    }

    @GetMapping("/pets/new")
    public String showFormForAdd(Owner owner, Model theModel) {
        Pet thePet = new Pet();
        owner.addPet(thePet);
        theModel.addAttribute("pet", thePet);

        return "pets/pet-form";
    }

    @PostMapping("/pets/save")
    public String savePet(@ModelAttribute("pet") Pet thePet, Owner theOwner) {
        theOwner.getPets().add(thePet);
        thePet.setOwner(theOwner);
        petService.save(thePet);

        return "redirect:/owners/list";
    }

    @GetMapping("/petDetails")
    public String petDetails(@RequestParam("petId") int theId, Model theModel) {
        Pet thePet = petService.findById(theId);
        theModel.addAttribute("pet", thePet);
        return "pets/pet-details";
    }

    @GetMapping("/pets/update")
    public String showFormForUpdate(@RequestParam("petId") int theId, Model theModel) {
        Pet thePet = petService.findById(theId);
        theModel.addAttribute("pet", thePet);
        return "pets/pet-form";
    }

    @GetMapping("/pets/delete")
    public String deletePet(@RequestParam("petId") int theId) {
        petService.deleteById(theId);
        return "redirect:/owners/list";
    }

}
