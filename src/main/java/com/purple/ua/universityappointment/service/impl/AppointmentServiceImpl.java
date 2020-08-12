package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.exception.AppointmentNotFoundException;
import com.purple.ua.universityappointment.exception.TimeConflictException;
import com.purple.ua.universityappointment.exception.UserNotFoundException;
import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.Status;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.AppointmentRepository;
import com.purple.ua.universityappointment.service.AppointmentService;
import com.purple.ua.universityappointment.service.LessonService;
import com.purple.ua.universityappointment.service.UserService;
import com.purple.ua.universityappointment.util.MailUtils;
import com.purple.ua.universityappointment.util.mapper.LessonMapper;
import com.purple.ua.universityappointment.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.PriceUtils.countPrice;
import static com.purple.ua.universityappointment.util.mapper.AppointmentMapper.INSTANCE;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserService userService;

    private final LessonService lessonService;

    private final MailUtils mailUtils;


    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto, User user, long lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        LocalDateTime startTime = appointmentDto.getFromDate();
        LocalDateTime endTime = appointmentDto.getToDate();
        timeCheck(lesson, startTime, endTime);
        if (appointmentRepository.findTimeOverlap(startTime, endTime, lessonId).isEmpty()) {
            long duration = Duration.between(startTime, endTime).toMinutes();
            AppointmentDto appointment = countPrice(lesson, duration, appointmentDto);
            User student = userService.getUserById(user.getId());
            appointment.setStudent(UserMapper.INSTANCE.toDto(student));
            appointment.setLesson(LessonMapper.INSTANCE.toDto(lesson));
            appointment.setStatus(Status.PENDING);
            Appointment save = appointmentRepository.save(INSTANCE.toEntity(appointment));
            // String lecturerEmail = lessonRepository.findById(id).get().getLecturer().getEmail();
            mailUtils.mailInput(student.getEmail(), "Appointment created!",
                    "To cancel your appointment, please click here : "
                            + mailUtils.getUrl() + "/appointment/delete?id=" + save.getId());

            mailUtils.mailInput(lesson.getLecturer().getEmail(), "Approve/decline appointment",
                    "You have received an appointment for: " + save.getFromDate() +
                            "from " + appointment.getStudent().getFirstName() + " " + appointment.getStudent().getLastName() +
                            "\n" + "To approve, please click here: " + mailUtils.getUrl() + "/appointment/status?id="
                            + save.getId() + "&status=APPROVED" +
                            "\n" + "To decline, please click here: " + mailUtils.getUrl() + "/appointment/status?id="
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
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto, long lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        Appointment appointment = appointmentRepository.findById(appointmentDto.getId()).orElseThrow(
                () -> new AppointmentNotFoundException(HttpStatus.NOT_FOUND, "Appointment with id: "
                        + appointmentDto.getId() + " not found"));
        LocalDateTime startTime = appointmentDto.getFromDate();
        LocalDateTime endTime = appointmentDto.getToDate();
        timeCheck(lesson, startTime, endTime);
        if (appointmentRepository.findTimeOverlapExclude(startTime, endTime, lessonId, appointment.getId()).isEmpty()) {
            long duration = Duration.between(startTime, endTime).toMinutes();
            appointmentDto.setFinalPrice(countPrice(lesson, duration, appointmentDto).getFinalPrice());
            appointmentDto.setLesson(LessonMapper.INSTANCE.toDto(lesson));
            String lecturerEmail = lesson.getLecturer().getEmail();
            Appointment save = appointmentRepository.save(INSTANCE.toEntity(appointmentDto));
            mailUtils.mailInput(lecturerEmail, "Approve/decline appointment",
                    "Appointment updated for: " + save.getFromDate() +
                            "from " + appointment.getStudent().getFirstName() + " " + appointment.getStudent().getLastName() +
                            "\n" + "To approve, please click here: " + mailUtils.getUrl() + "/appointment/status?id="
                            + save.getId() + "&status=APPROVED" +
                            "\n" + "To decline, please click here: " + mailUtils.getUrl() + "/appointment/status?id="
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
        mailUtils.mailInput(save.getStudent().getEmail(), "Appointment status!",
                "Your appointment for " + save.getLesson().getFromDate() + " is: " + save.getStatus());
        return INSTANCE.toDto(save);
    }

    public void timeCheck(Lesson lesson, LocalDateTime startTime, LocalDateTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "DateFrom is after ToDate");
        }
        if ((startTime.isBefore(lesson.getFromDate()) || endTime.isAfter(lesson.getToDate()))) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date doesn't match available time");
        }
    }
}
