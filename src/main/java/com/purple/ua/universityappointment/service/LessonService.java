package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;

import java.util.List;

public interface LessonService {

    Lesson getLessonById(long id);

    List<Lesson> getAllLesson(User user);

    List<Lesson> getLessonsByLecturerFirstNameAndLastName(String firstName, String secondName);

    List<Lesson> getLessonsByLecturerId(long id);

    Lesson getLessonByLessonName(String name);

    Lesson createLesson(Lesson lesson, User user);

    Lesson updateLesson(Lesson lesson);

    Lesson deleteLesson(long id);



}
