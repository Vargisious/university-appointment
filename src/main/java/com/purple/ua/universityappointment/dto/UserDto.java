package com.purple.ua.universityappointment.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

public class UserDto {

    private Long id;

    @NotNull
    @Length(min = 2)
    private String firstName;

    @NotNull
    @Length(min = 2)
    private String lastName;

    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;


    private String login;

    @Past
    @NotNull
    private LocalDate birthday;

    @NotNull
    @Length(min=8)
    private String password;

    private String roles;

    private List<LessonDto> lessons;

    private List<AppointmentDto> appointments;

}
