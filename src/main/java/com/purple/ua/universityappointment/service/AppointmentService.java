package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.model.Appointment;

import java.util.List;

public interface AppointmentService {

    List<Appointment> getAllAppointments();

    List<Appointment> getAllAppointmentsByLecturerFirstNameAndLastName(String firstName, String lastName);

    Appointment createAppointment(Appointment appointment);

    Appointment updateAppointment(Appointment appointment);

    Appointment deleteAppointment(Appointment appointment);

    List<Appointment> getAllByStudentId(long id);

}
