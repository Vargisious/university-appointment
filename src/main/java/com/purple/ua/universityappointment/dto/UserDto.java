package com.purple.ua.universityappointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class UserDto {

    private Long id;

    @NotNull
    @Length(min = 2, message = "Field first name should have more than 1 character")
    private String firstName;

    @NotNull
    @Length(min = 2, message = "Field last name should have more than 1 character")
    private String lastName;

    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotNull
    @Length(min = 2, message = "Field username should have more than 1 character")
    private String userName;

    @Past
    @NotNull
    private LocalDate birthday;

    @NotNull
    @Length(min = 8, message = "Password should be at least 8 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private String roles;

}
