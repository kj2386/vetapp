package com.example.vetapp.controller;

import com.example.vetapp.entity.Owner;

import com.example.vetapp.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    private OwnerService ownerService;


    public OwnerController(OwnerService theOwnerService) {
        ownerService = theOwnerService;
    }

    @GetMapping("/list")
    public String listOwners(Model theModel) {

        List<Owner> theOwners = ownerService.findAll();

        theModel.addAttribute("owners", theOwners);

        return "owners/list-owners";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        Owner theOwner = new Owner();
        theModel.addAttribute("owner", theOwner);

        return "owners/owner-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("ownerId") int theId, Model theModel) {
        Owner theOwner = ownerService.findById(theId);
        theModel.addAttribute("owner", theOwner);
        return "owners/owner-form";
    }

    @PostMapping("/save")
    public String saveOwner(@ModelAttribute("owner") Owner theOwner) {
        if (theOwner.getId() == 0) {
            ownerService.save(theOwner);
            return "redirect:/owners/list";
        }
        Owner currOwner = ownerService.findById(theOwner.getId());
        theOwner.setPets(currOwner.getPets());
        ownerService.save(theOwner);
        return "redirect:/owners/list";
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("ownerId") int theId) {
        ownerService.deleteById(theId);
        return "redirect:/owners/list";
    }

    @GetMapping("/ownerDetails")
    public String ownerDetails(@RequestParam("ownerId") int theId, Model theModel) {
        Owner theOwner = ownerService.findById(theId);
        theModel.addAttribute("owner", theOwner);
        return "owners/owner-details";

    }
}
