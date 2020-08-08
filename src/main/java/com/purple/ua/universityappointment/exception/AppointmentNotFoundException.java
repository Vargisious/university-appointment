package com.purple.ua.universityappointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AppointmentNotFoundException extends ResponseStatusException {
    public AppointmentNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
