package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.UserService;
import com.purple.ua.universityappointment.service.impl.EmailSenderService;
import com.purple.ua.universityappointment.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        userService.saveUser(INSTANCE.dtoToEntity(userDto));
        return new ResponseEntity(userDto, HttpStatus.CREATED);
    }


    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) throws Exception {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            Optional<User> optionalUser = userRepository.findByEmail(token.getUser().getEmail());
            User user = optionalUser.get();
            user.setEnabled(true);
            userRepository.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            throw new Exception(String.format("Link is broken"));
        }

        return new ResponseEntity("Email confirmed",HttpStatus.OK);
    }


}
