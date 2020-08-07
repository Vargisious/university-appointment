package com.purple.ua.universityappointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.purple.ua.universityappointment.model.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDto {

    private Long id;

    private LessonDto lesson;

    private UserDto student;

    private Status status;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fromDate;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toDate;

    private int finalPrice;

}
