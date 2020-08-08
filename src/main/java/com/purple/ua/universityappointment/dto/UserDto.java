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

    @NotNull(message = "Please fill in the first name field")
    @Length(min = 2)
    private String firstName;

    @NotNull(message = "Please fill in the last name field")
    @Length(min = 2)
    private String lastName;

    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotNull(message = "Please fill in the username field")
    @Length(min = 2, message = "User name should have more than 1 character  ")
    private String userName;

    @Past
    @NotNull(message = "Please fill in the birthday field")
    private LocalDate birthday;

    @NotNull(message = "Please fill in the birthday field")
    @Length(min=8,message = "Password should have more than 7 characters ")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private String roles;

}
