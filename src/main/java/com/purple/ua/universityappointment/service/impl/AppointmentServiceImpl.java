package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.repository.AppointmentRepository;
import com.purple.ua.universityappointment.service.AppointmentService;
import com.purple.ua.universityappointment.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private LessonService lessonService;

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAllAppointmentsByLecturerFirstNameAndLastName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        LocalDateTime startTime = appointment.getFromDate();
        LocalDateTime endTime = appointment.getToDate();
        LocalDateTime lessonFromDate = appointment.getLesson().getFromDate();
        LocalDateTime lessonToDate = appointment.getLesson().getToDate();
        Long id = appointment.getLesson().getId();


        if (!endTime.isAfter(startTime)) {
            throw new RuntimeException(String.format("DateFrom is after ToDate"));
        }
        if ((startTime.isBefore(lessonFromDate) || endTime.isAfter(lessonToDate))) {
            throw new RuntimeException(String.format("The date doesn't match available time"));
        }
//        if (appointmentRepository.findAll().stream().anyMatch(appo -> appointment.getFromDate().isBefore(lessonToDate) &&
//                appo.getToDate().isAfter(lessonFromDate)&&appo.getLesson().getId().equals(id))) {
//            throw new RuntimeException(String.format("The date is overlapping another appointment"));
//       }

        if (appointmentRepository.findTimeOverlap(startTime, endTime, id).isEmpty()) {
            appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException(String.format("The date is overlapping another appointment"));

        }
        return appointment;
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
        return appointment;
    }

    @Override
    public Appointment deleteAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
        return appointment;
    }

    @Override
    public List<Appointment> getAllByStudentId(long id) {
       return appointmentRepository.getAllByStudentId(id);
    }

}
