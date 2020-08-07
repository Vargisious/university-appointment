package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.UserService;
import com.purple.ua.universityappointment.util.CycleAvoidingMappingContext;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.UserMapper.INSTANCE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;


    @GetMapping("/info")
    ResponseEntity<UserDto> getUser(@AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {
        User user = userService.getUserById(userDetails.getUser().getId());
        return new ResponseEntity<>(INSTANCE.toDto(user, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity(INSTANCE.listToDto(users, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }


    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        userService.saveUser(INSTANCE.toEntity(userDto));
        return new ResponseEntity(userDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(INSTANCE.toEntity(userDto));
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<UserDto> deleteUser(@AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {
        User user = userService.deleteUserById(userDetails.getUser().getId());
        return new ResponseEntity<>(INSTANCE.toDto(user,new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/lecturer/all")
    ResponseEntity<List<User>> getAllLecturers() {
        List<User> lecturers = userService.getAllLecturers();
        return new ResponseEntity(INSTANCE.listToDto(lecturers, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/student/all")
    ResponseEntity<List<User>> getAllStudents() {
        List<User> lecturers = userService.getAllStudents();
        return new ResponseEntity(INSTANCE.listToDto(lecturers, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }


    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) throws Exception {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Optional<User> optionalUser = userRepository.findByEmail(token.getUser().getEmail());
            User user = optionalUser.get();
            user.setEnabled(true);
            userRepository.save(user);
            modelAndView.setViewName("accountVerified");
        } else {
            throw new Exception(String.format("Link is broken"));
        }

        return new ResponseEntity("Email confirmed", HttpStatus.OK);
    }


}
