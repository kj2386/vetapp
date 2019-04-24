package com.example.vetapp.dao;

import com.example.vetapp.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    public List<Owner> findAllByOrderByLastNameAsc();

}
