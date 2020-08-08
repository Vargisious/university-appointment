package com.purple.ua.universityappointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FieldAlreadyInUseException extends ResponseStatusException {
    public FieldAlreadyInUseException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
