package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.model.User;

import java.util.List;

public interface AppointmentService {

    AppointmentDto createAppointment(AppointmentDto appointmentDto, User user, long lessonId);

    AppointmentDto getAppointmentById(long id);

    AppointmentDto updateAppointment(AppointmentDto appointmentDto, long lessonId);

    AppointmentDto deleteAppointmentById(long id );

    List<AppointmentDto> getAllByUser(User user);

    List<AppointmentDto> getAllByLessonId(long id);

    AppointmentDto updateStatus(long id, Status status);

}
