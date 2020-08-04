package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static com.purple.ua.universityappointment.util.AppointmentMapper.INSTANCE;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {


    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/create")
    ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        appointmentService.createAppointment(INSTANCE.toEntity(appointmentDto));
        return new ResponseEntity(appointmentDto, HttpStatus.CREATED);
    }

    @GetMapping("/student/{id}")
    ResponseEntity<List<AppointmentDto>> getAllAppointmentsByUserId(@PathVariable long id){
        List<AppointmentDto> appointmentDtos = INSTANCE.listToDto(appointmentService.getAllByStudentId(id));
        return new ResponseEntity(appointmentDtos,HttpStatus.OK);
    }

}
