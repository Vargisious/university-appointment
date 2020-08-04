package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.model.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    Optional<Lesson> getLessonById(long id);

    List<Lesson> getLessonsByLecturerFirstNameAndLastName(String firstName, String secondName);

    Lesson getLessonByLessonName(String name);

    Lesson createLesson(Lesson lesson);

    Lesson updateLesson(Lesson lesson);

    Lesson deleteLesson(Lesson lesson);



}
