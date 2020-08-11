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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "lesson_table")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "price_per_hour")
    private int price;

    @Column(name = "discount")
    private double discount;

    @Column(name = "discount_start")
    private int discountStart;

    @ManyToOne()
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "lesson")
    private List<Appointment> appointment;

    @Column(name = "date_from")
    private LocalDateTime fromDate;

    @Column(name = "date_to")
    private LocalDateTime toDate;

}
