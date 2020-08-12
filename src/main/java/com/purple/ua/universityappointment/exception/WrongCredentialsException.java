package com.purple.ua.universityappointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongCredentialsException extends ResponseStatusException {
    public WrongCredentialsException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
