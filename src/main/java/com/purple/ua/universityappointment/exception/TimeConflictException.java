package com.purple.ua.universityappointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TimeConflictException extends ResponseStatusException {
    public TimeConflictException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
