package com.purple.ua.universityappointment.repository;

import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query(value = "SELECT l FROM lesson_table l  WHERE l.fromDate <= :toDate AND l.toDate>= :fromDate AND" +
            " l.lecturer.id = :userId")
    List<Lesson> findTimeOverlap(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
                                 @Param("userId") Long userId);

    @Query(value = "SELECT l FROM lesson_table l  WHERE l.fromDate <= :toDate AND l.toDate>= :fromDate AND" +
            " l.lecturer.id = :userId AND NOT l.id =:lessonId ")
    List<Lesson> findTimeOverlapExclude(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
                                        @Param("userId") Long userId, @Param("lessonId") long lessonId);

    List<Lesson> findByLecturer(User user);

    List<Lesson> findByFieldOfStudy(String name);

    List<Lesson> findByLecturerId(long id);

}
