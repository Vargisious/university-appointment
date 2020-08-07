package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.AppointmentService;
import com.purple.ua.universityappointment.util.AppointmentMapper;
import com.purple.ua.universityappointment.util.CycleAvoidingMappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PutMapping("/status")
    ResponseEntity<AppointmentDto> approveAppointment(@RequestParam long id, @RequestParam Status status) {
        Appointment appointment = appointmentService.updateStatus(id, status);
        return new ResponseEntity<>(AppointmentMapper.INSTANCE.toDto(appointment, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/lesson")
    ResponseEntity<List<AppointmentDto>> getAllAppointmentsByLessonId(@RequestParam long id) {
        List<Appointment> appointments = appointmentService.getAllByLessonId(id);
        return new ResponseEntity<>(INSTANCE.listToDto(appointments, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/related")
    ResponseEntity<List<AppointmentDto>> getAllRelatedAppointments(@AuthenticationPrincipal MyUserDetails userDetails) {
        List<Appointment> appointments = appointmentService.getAllByUser(userDetails.getUser());
        return new ResponseEntity<>(INSTANCE.listToDto(appointments, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<AppointmentDto> getAppointmentById(@RequestParam long id) {
        Appointment appointment = appointmentService.getAppointmentById(id).get();
        return new ResponseEntity<>(AppointmentMapper.INSTANCE.toDto(appointment, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<AppointmentDto> deleteAppointment(@RequestParam long id) {
        Appointment appointment = appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>(INSTANCE.toDto(appointment, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @PostMapping("/create")
    ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        Appointment appointment = INSTANCE.toEntity(appointmentDto);
        Appointment app = appointmentService.createAppointment(appointment, userDetails.getUser());
        return new ResponseEntity<>(INSTANCE.toDto(app, new CycleAvoidingMappingContext()), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    ResponseEntity<AppointmentDto> updateAppointment(@RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.updateAppointment(INSTANCE.toEntity(appointmentDto));
        return new ResponseEntity<>(INSTANCE.toDto(appointment, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }


}
