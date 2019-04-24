package com.example.vetapp.service;

import com.example.vetapp.dao.PetRepository;
import com.example.vetapp.entity.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private PetRepository petRepository;

    @Autowired
    public  PetServiceImpl(PetRepository thePetRepository) {
        petRepository = thePetRepository;
    }

    @Override
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    @Override
    public Pet findById(int theId) {
        Optional<Pet> result = petRepository.findById(theId);

        Pet thePet = null;

        if (result.isPresent()) {
            thePet = result.get();
        } else {
            throw new RuntimeException("Did not find pet Id - " + theId);
        }
        return thePet;
    }

    @Override
    public void save(Pet thePet) {
        petRepository.save(thePet);
    }

    @Override
    public void deleteById(int theId) {
        petRepository.deleteById(theId);
    }
}
