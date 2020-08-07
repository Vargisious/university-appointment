package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.AppointmentRepository;
import com.purple.ua.universityappointment.repository.LessonRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private LessonRepository lessonRepository;


    @Override
    public Appointment createAppointment(Appointment appointment, User user) {
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
        if (appointmentRepository.findTimeOverlap(startTime, endTime, id).isEmpty()) {
            User student = userRepository.findById(user.getId()).get();
            appointment.setStudent(student);
            Appointment save = appointmentRepository.save(appointment);

            emailSenderService.mailInput(student.getEmail(), "Appointment created!", "anotherdayofbill2017@gmail.com",
                    "To cancel your appointment, please click here : "
                            + "http://localhost:8080/appointment/delete?id=" + save.getId());

            emailSenderService.mailInput(save.getLesson().getLecturer().getEmail(), "Approve/decline appointment",
                    "anotherdayofbill2017@gmail.com", "You have received an appointment for: " + save.getFromDate() +
                            "from " + appointment.getStudent().getFirstName() + " " + appointment.getStudent().getLastName() +
                            "\n" + "To approve, please click here: " + "http://localhost:8080/appointment/status?id="
                            + save.getId() + "&status=APPROVED" +
                            "\n" + "To decline, please click here: " + "http://localhost:8080/appointment/status?id="
                            + save.getId() + "&status=DECLINED");
            return save;

        } else {
            throw new RuntimeException(String.format("The date is overlapping another appointment"));

        }

    }

    @Override
    public Optional<Appointment> getAppointmentById(long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        LocalDateTime appointmentFromDate = appointment.getFromDate();
        LocalDateTime appointmentToDate = appointment.getToDate();
        LocalDateTime lessonFromDate = appointment.getLesson().getFromDate();
        LocalDateTime lessonToDate = appointment.getLesson().getToDate();
        Long id = appointment.getLesson().getId();

        if (!appointmentToDate.isAfter(appointmentFromDate)) {
            throw new RuntimeException(String.format("DateFrom is after ToDate"));
        }
        if ((appointmentFromDate.isBefore(lessonFromDate) || appointmentToDate.isAfter(lessonToDate))) {
            throw new RuntimeException(String.format("The date doesn't match available time"));
        }
        if (appointmentRepository.findTimeOverlap(appointmentFromDate, appointmentToDate, id).isEmpty()) {
            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException(String.format("The date is overlapping another appointment"));

        }
    }

    @Override
    public Appointment deleteAppointmentById(long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            appointmentRepository.deleteById(id);
            return appointment.get();
        } else {
            throw new RuntimeException(String.format("Appointment with id: " + id + " not found"));
        }
    }

    @Override
    public List<Appointment> getAllByUser(User user) {
        return appointmentRepository.getAllByStudent(user);
    }

    @Override
    public List<Appointment> getAllByLessonId(long id) {
        return appointmentRepository.getAllByLessonId(id);
    }

    @Override
    public Appointment updateStatus(long id, Status status) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            appointment.get().setStatus(status);
            Appointment save = appointmentRepository.save(appointment.get());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            emailSenderService.mailInput(save
                            .getStudent().getEmail(), "Appointment status!", "anotherdayofbill2017@gmail.com",
                    "Your appointment for " + save.getLesson().getFromDate() + " is: " + save.getStatus());
            return save;

        } else {
            throw new RuntimeException(String.format("Appointment with id: " + id + " not found"));
        }

    }

}
