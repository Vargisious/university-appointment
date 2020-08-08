package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.exception.AppointmentNotFoundException;
import com.purple.ua.universityappointment.exception.LessonNotFoundException;
import com.purple.ua.universityappointment.exception.TimeConflictException;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.AppointmentRepository;
import com.purple.ua.universityappointment.repository.LessonRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.AppointmentService;
import com.purple.ua.universityappointment.util.LessonMapper;
import com.purple.ua.universityappointment.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.AppointmentMapper.INSTANCE;

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

    @LocalServerPort
    private int port;

    String ip = InetAddress.getLocalHost().getHostAddress();

    public AppointmentServiceImpl() throws UnknownHostException {
    }

    @Override
    @Transactional
    public AppointmentDto createAppointment(AppointmentDto appointmentDto, User user, long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new LessonNotFoundException(HttpStatus.NOT_FOUND, "Lesson with id: " + lessonId + " not found"));
        LocalDateTime startTime = appointmentDto.getFromDate();
        LocalDateTime endTime = appointmentDto.getToDate();
        LocalDateTime lessonFromDate = lesson.getFromDate();
        LocalDateTime lessonToDate = lesson.getToDate();
        //System.out.println(Duration.between(startTime, endTime).toMinutes());

        if (!endTime.isAfter(startTime)) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "DateFrom is after ToDate");
        }
        if ((startTime.isBefore(lessonFromDate) || endTime.isAfter(lessonToDate))) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date doesn't match available time");
        }
        if (appointmentRepository.findTimeOverlap(startTime, endTime, lessonId).isEmpty()) {
            User student = userRepository.findById(user.getId()).get();
            appointmentDto.setStudent(UserMapper.INSTANCE.toDto(student));
            appointmentDto.setLesson(LessonMapper.INSTANCE.toDto(lesson));
            appointmentDto.setStatus(Status.PENDING);
            Appointment save = appointmentRepository.save(INSTANCE.toEntity(appointmentDto));
            // String lecturerEmail = lessonRepository.findById(id).get().getLecturer().getEmail();
            mailInput(student.getEmail(), "Appointment created!",
                    "To cancel your appointment, please click here : "
                            + "http://"+ip+":"+port+"/appointment/delete?id=" + save.getId());

            mailInput(lesson.getLecturer().getEmail(), "Approve/decline appointment",
                    "You have received an appointment for: " + save.getFromDate() +
                            "from " + appointmentDto.getStudent().getFirstName() + " " + appointmentDto.getStudent().getLastName() +
                            "\n" + "To approve, please click here: " + "http://"+ip+":"+port+"/appointment/status?id="
                            + save.getId() + "&status=APPROVED" +
                            "\n" + "To decline, please click here: " + "http://"+ip+":"+port+"/appointment/status?id="
                            + save.getId() + "&status=DECLINED");
            return INSTANCE.toDto(save);

        } else {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date is overlapping another appointment");
        }
    }


    @Override
    public AppointmentDto getAppointmentById(long id) throws UserNotFoundException {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return INSTANCE.toDto(appointment.orElseThrow(
                () -> new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointment with id: " + id + " not found")
        ));
    }

    @Override
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
        appointmentRepository.findById(appointmentDto.getId()).orElseThrow(
                () -> new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointment with id: "
                        + appointmentDto.getId() + " not found"));
        LocalDateTime appointmentFromDate = appointmentDto.getFromDate();
        LocalDateTime appointmentToDate = appointmentDto.getToDate();
        LocalDateTime lessonFromDate = appointmentDto.getLesson().getFromDate();
        LocalDateTime lessonToDate = appointmentDto.getLesson().getToDate();
        Long id = appointmentDto.getLesson().getId();

        if (!appointmentToDate.isAfter(appointmentFromDate)) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "DateFrom is after ToDate");
        }
        if ((appointmentFromDate.isBefore(lessonFromDate) || appointmentToDate.isAfter(lessonToDate))) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date doesn't match available time");
        }
        if (appointmentRepository.findTimeOverlap(appointmentFromDate, appointmentToDate, id).isEmpty()) {
            String lecturerEmail = lessonRepository.findById(id).get().getLecturer().getEmail();
            Appointment save = appointmentRepository.save(INSTANCE.toEntity(appointmentDto));
            mailInput(lecturerEmail, "Approve/decline appointment",
                    "Appointment updated for: " + save.getFromDate() +
                            "from " + appointmentDto.getStudent().getFirstName() + " " + appointmentDto.getStudent().getLastName() +
                            "\n" + "To approve, please click here: " + "http://localhost:8080/appointment/status?id="
                            + save.getId() + "&status=APPROVED" +
                            "\n" + "To decline, please click here: " + "http://localhost:8080/appointment/status?id="
                            + save.getId() + "&status=DECLINED");
            return INSTANCE.toDto(save);
        } else {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date is overlapping another appointment");
        }
    }


    @Override
    public AppointmentDto deleteAppointmentById(long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointment with id: " + id + " not found"));
        appointmentRepository.deleteById(id);
        return INSTANCE.toDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllByUser(User user) {
        List<Appointment> appointments = appointmentRepository.getAllByStudent(user);
        if (!appointments.isEmpty()) {
            return INSTANCE.listToDto(appointments);
        } else {
            throw new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointments for User: " + user.getUserName() + " not found");
        }
    }

    @Override
    public List<AppointmentDto> getAllByLessonId(long id) {
        List<Appointment> appointments = appointmentRepository.getAllByLessonId(id);
        if (!appointments.isEmpty()) {
            return INSTANCE.listToDto(appointments);
        }
        throw new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointments for lesson with  id: " + id + " not found");
    }

    @Override
    public AppointmentDto updateStatus(long id, Status status) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointment with id: " + id + " not found"));
        appointment.setStatus(status);
        Appointment save = appointmentRepository.save(appointment);
        mailInput(save.getStudent().getEmail(), "Appointment status!",
                "Your appointment for " + save.getLesson().getFromDate() + " is: " + save.getStatus());
        return INSTANCE.toDto(save);
    }


    public void mailInput(String email, String subject, String text)  {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        emailSenderService.sendEmail(mailMessage);
    }

}
