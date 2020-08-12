package com.purple.ua.universityappointment.security;

import com.purple.ua.universityappointment.exception.WrongCredentialsException;
import com.purple.ua.universityappointment.security.model.AuthenticationRequest;
import com.purple.ua.universityappointment.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtils;

    private final MyUserDetailsService userDetailsService;

    public String parseAuthenticationRequest(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new WrongCredentialsException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        return jwtUtils.generateToken(userDetails);
    }
}
