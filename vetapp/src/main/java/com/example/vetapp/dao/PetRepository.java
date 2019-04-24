package com.example.vetapp.dao;

import com.example.vetapp.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetRepository extends JpaRepository<Pet, Integer> {



}
