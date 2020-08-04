package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.service.AppointmentService;
import com.purple.ua.universityappointment.service.LessonService;
import com.purple.ua.universityappointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.time.LocalDate;


@RestController
public class all {

    @GetMapping
    public String home(){
        return ("<h1>Welcome</h1>");
    }

}
