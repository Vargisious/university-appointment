package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.model.ConfirmationToken;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.ConfirmationTokenRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.security.AuthenticationService;
import com.purple.ua.universityappointment.security.model.AuthenticationRequest;
import com.purple.ua.universityappointment.security.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return new ResponseEntity<>(new AuthenticationResponse(authenticationService.parseAuthenticationRequest(authenticationRequest)),
                HttpStatus.OK);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken) throws Exception {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Optional<User> optionalUser = userRepository.findByEmail(token.getUser().getEmail());
            User user = optionalUser.get();
            user.setEnabled(true);
            userRepository.save(user);
        } else {
            throw new Exception(String.format("Link is broken"));
        }

        return new ResponseEntity<>("Email confirmed", HttpStatus.OK);
    }
}
