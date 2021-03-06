package com.purple.ua.universityappointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableAsync
@EnableWebSecurity
@EnableJpaRepositories
@SpringBootApplication
public class UniversityAppointmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityAppointmentApplication.class, args);
    }

}

