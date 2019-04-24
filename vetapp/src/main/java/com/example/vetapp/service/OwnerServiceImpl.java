package com.example.vetapp.service;

import com.example.vetapp.dao.OwnerRepository;
import com.example.vetapp.entity.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImpl implements OwnerService {

    private OwnerRepository ownerRepository;

    @Autowired
    public OwnerServiceImpl(OwnerRepository theOwnerRepository) {
        ownerRepository = theOwnerRepository;
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Owner findById(int theId) {
        Optional<Owner> result = ownerRepository.findById(theId);

        Owner theOwner = null;

        if (result.isPresent()) {
            theOwner = result.get();
        } else {
            throw new RuntimeException("Did not find owner Id - " + theId);
        }
        return theOwner;
    }

    @Override
    public void save(Owner theOwner) {
        ownerRepository.save(theOwner);
    }

    @Override
    public void deleteById(int theId) {
        ownerRepository.deleteById(theId);
    }




}
