package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.LessonRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Lesson getLessonById(long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.orElseThrow(() -> {
            throw new RuntimeException(String.format("Lesson with id: " + id + "not found"));
        });
    }

    @Override
    public List<Lesson> getAllLesson(User user) {
        return lessonRepository.findByLecturer(user);
    }

    @Override
    public List<Lesson> getLessonsByLecturerFirstNameAndLastName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return lessonRepository.findByLecturer(user);
    }

    @Override
    public List<Lesson> getLessonsByLecturerId(long id) {
        return lessonRepository.findByLecturerId(id);
    }

    @Override
    public Lesson getLessonByLessonName(String name) {
        return lessonRepository.findByLessonName(name);
    }

    @Override
    @Transactional
    public Lesson createLesson(Lesson lesson, User user) {
        LocalDateTime fromDate = lesson.getFromDate();
        LocalDateTime toDate = lesson.getToDate();
        if (!toDate.isAfter(fromDate)) {
            throw new RuntimeException(String.format("DateFrom is after ToDate"));
        }
        if (lessonRepository.findTimeOverlap(fromDate, toDate, user.getId()).isEmpty()) {
            User lecturer = userRepository.findById(user.getId()).get();
            lesson.setLecturer(lecturer);
            return lessonRepository.save(lesson);
        } else {
            throw new RuntimeException(String.format("The date is overlapping another lesson"));
        }
    }

    @Override
    public Lesson updateLesson(Lesson lesson) {
        LocalDateTime fromDate = lesson.getFromDate();
        LocalDateTime toDate = lesson.getToDate();
        if (!toDate.isAfter(fromDate)) {
            throw new RuntimeException(String.format("DateFrom is after ToDate"));
        }
        if (lessonRepository.findTimeOverlap(fromDate, toDate, lesson.getLecturer().getId()).isEmpty()) {
            return lessonRepository.save(lesson);
        } else {
            throw new RuntimeException(String.format("The date is overlapping another lesson"));
        }

    }

    @Override
    public Lesson deleteLesson(long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        if (lesson.isPresent()) {
            lessonRepository.deleteById(id);
            return lesson.get();
        } else {
            throw new RuntimeException(String.format("Lesson with id: " + id + " not found"));

        }
    }
}
