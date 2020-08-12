package com.purple.ua.universityappointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column
    private String userName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String roles;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "lecturer")
    private List<Lesson> lessons;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "student")
    private List<Appointment> appointments;

    private boolean isEnabled;

}
