package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    ResponseEntity<UserDto> getUser(@AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {
        UserDto user = userService.getUserById(userDetails.getUser().getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.saveUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<UserDto> deleteUser(@AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {
        UserDto user = userService.deleteUserById(userDetails.getUser().getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/lecturer/all")
    ResponseEntity<List<UserDto>> getAllLecturers() {
        List<UserDto> lecturers = userService.getAllLecturers();
        return new ResponseEntity<>(lecturers, HttpStatus.OK);
    }

    @GetMapping("/student/all")
    ResponseEntity<List<UserDto>> getAllStudents() {
        List<UserDto> students = userService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
