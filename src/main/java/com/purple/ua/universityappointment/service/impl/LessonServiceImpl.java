package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.LessonRepository;
import com.purple.ua.universityappointment.repository.UserRepository;
import com.purple.ua.universityappointment.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<Lesson> getLessonById(long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<Lesson> getLessonsByLecturerFirstNameAndLastName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return lessonRepository.findByLecturer(user);
    }

    @Override
    public Lesson getLessonByLessonName(String name) {
        return lessonRepository.findByLessonName(name);
    }

    @Override
    public Lesson createLesson(Lesson lesson) {
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    public Lesson updateLesson(Lesson lesson) {
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    public Lesson deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
        return lesson;
    }
}
