package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.AppointmentService;
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

@RestController
@RequestMapping("/appointment")
public class AppointmentController {


    @Autowired
    AppointmentService appointmentService;

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PutMapping("/status")
    ResponseEntity<AppointmentDto> approveAppointment(@RequestParam long id, @RequestParam Status status) {
        AppointmentDto appointment = appointmentService.updateStatus(id, status);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/lesson")
    ResponseEntity<List<AppointmentDto>> getAllAppointmentsByLessonId(@RequestParam long id) {
        List<AppointmentDto> appointments = appointmentService.getAllByLessonId(id);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/related")
    ResponseEntity<List<AppointmentDto>> getAllRelatedAppointments(@AuthenticationPrincipal MyUserDetails userDetails) {
        List<AppointmentDto> appointments = appointmentService.getAllByUser(userDetails.getUser());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<AppointmentDto> getAppointmentById(@RequestParam long id) throws UserNotFoundException {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<AppointmentDto> deleteAppointment(@RequestParam long id) {
        AppointmentDto appointment = appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/create")
    ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto,
                                                     @RequestParam long lessonId,
                                                     @AuthenticationPrincipal MyUserDetails userDetails) {
        AppointmentDto appointment = appointmentService.createAppointment(appointmentDto, userDetails.getUser(), lessonId);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    ResponseEntity<AppointmentDto> updateAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto appointment = appointmentService.updateAppointment(appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }


}
