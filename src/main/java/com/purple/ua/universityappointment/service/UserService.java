package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<com.purple.ua.universityappointment.model.User> getUserById(Long id);

    Optional<User> getUserByLogin(String login);

    List<User> getAllLecturers();

    List<User> getAllStudents();

    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    User deleteUser(User user);
}
