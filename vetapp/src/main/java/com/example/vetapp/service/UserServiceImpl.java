package com.example.vetapp.service;

import com.example.vetapp.dao.RoleRepository;
import com.example.vetapp.dao.UserRepository;
import com.example.vetapp.entity.Role;
import com.example.vetapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void save(User theUser) {
        theUser.setPassword(bCryptPasswordEncoder.encode(theUser.getPassword()));
        theUser.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        theUser.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(theUser);

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
