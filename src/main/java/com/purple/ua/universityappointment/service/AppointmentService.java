package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.model.User;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment, User user);

    Optional<Appointment> getAppointmentById(long id);

    Appointment updateAppointment(Appointment appointment);

    Appointment deleteAppointmentById(long id );

    List<Appointment> getAllByUser(User user);

    List<Appointment> getAllByLessonId(long id);

    Appointment updateStatus(long id, Status status);

}
