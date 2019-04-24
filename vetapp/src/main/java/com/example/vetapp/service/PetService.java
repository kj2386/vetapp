package com.example.vetapp.service;

import com.example.vetapp.entity.Pet;

import java.util.List;

public interface PetService {

    public List<Pet> findAll();

    public Pet findById(int theId);

    public void save(Pet thePet);

    public void deleteById(int theId);
}
