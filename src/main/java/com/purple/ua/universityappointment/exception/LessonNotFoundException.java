package com.purple.ua.universityappointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LessonNotFoundException extends ResponseStatusException {
    public LessonNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
