package com.example.vetapp.service;

import com.example.vetapp.entity.User;


public interface UserService {


    public void save(User theUser);


    public User findByEmail(String email);
}
