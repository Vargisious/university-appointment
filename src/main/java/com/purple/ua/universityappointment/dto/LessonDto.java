package com.purple.ua.universityappointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class LessonDto {

    private Long id;

    @NotNull
    @Length(min = 2)
    private String lessonName;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());


    private int price;

    private UserDto lecturer;

    private List<AppointmentDto> appointment;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fromDate;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toDate;

}
