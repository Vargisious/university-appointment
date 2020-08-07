package com.purple.ua.universityappointment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "lesson_name")
    private String lessonName;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "price_per_hour")
    private int price;

    @ManyToOne(/*cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH}*/)
    @JoinColumn(name = "lecturer_id")
    @JsonManagedReference(value = "lesson")
    private User lecturer;

    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "lesson")
    @JsonBackReference(value = "forLesson")
    private List<Appointment> appointment;

    @Column(name = "date_from")
    private LocalDateTime fromDate;

    @Column(name = "date_to")
    private LocalDateTime toDate;

}
