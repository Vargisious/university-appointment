package com.purple.ua.universityappointment.repository;

import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByLecturer(User user);

    Lesson findByLessonName(String name);
}
