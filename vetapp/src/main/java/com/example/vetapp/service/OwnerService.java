package com.example.vetapp.service;

import com.example.vetapp.entity.Owner;

import java.util.List;

public interface OwnerService {

    public List<Owner> findAll();

    public Owner findById(int theId);

    public void save(Owner theOwner);

    public void deleteById(int theId);


}
