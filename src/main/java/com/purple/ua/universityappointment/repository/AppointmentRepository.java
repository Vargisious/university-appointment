package com.purple.ua.universityappointment.repository;

import com.purple.ua.universityappointment.model.Appointment;
import com.purple.ua.universityappointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT a FROM appointment_table a  WHERE a.fromDate <= :toDate AND" +
            " a.toDate>= :fromDate AND a.lesson.id = :lessonId")
    List<Appointment> findTimeOverlap(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
                                      @Param("lessonId") Long lessonId);


    @Query(value = "SELECT a FROM appointment_table a WHERE a.fromDate <= :toDate AND" +
            " a.toDate>= :fromDate AND a.lesson.id = :lessonId AND NOT a.id=:appointmentId")
    List<Appointment> findTimeOverlapExclude(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
                                             @Param("lessonId") long lessonId, @Param("appointmentId") long appointmentId);

    List<Appointment> getAllByStudent(User user);

    List<Appointment> getAllByLessonId(long id);

}
