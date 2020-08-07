package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id) throws UserNotFoundException;


    List<User> getAllLecturers();

    List<User> getAllStudents();

    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    User deleteUserById(long  id) throws UserNotFoundException;

}
