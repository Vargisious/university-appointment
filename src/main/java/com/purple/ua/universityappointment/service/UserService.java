package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.User;

import java.util.List;

public interface UserService {

    UserDto getUser(long id) throws UserNotFoundException;

    User getUserById(long id);

    List<UserDto> getAllLecturers();

    List<UserDto> getAllStudents();

    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto user);

    UserDto updateUser(UserDto user);

    UserDto deleteUserById(long  id) throws UserNotFoundException;

}
