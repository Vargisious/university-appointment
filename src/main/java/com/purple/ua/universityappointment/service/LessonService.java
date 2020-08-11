package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;

import java.util.List;

public interface LessonService {

    LessonDto getLesson(long id);

    Lesson getLessonById(long id);

    List<LessonDto> getAllLesson(User user);

    List<LessonDto> getLessonsByLecturerId(long id);

    List<LessonDto> getLessonByFieldOfStudy(String name);

    LessonDto createLesson(LessonDto lessonDto, User user);

    LessonDto updateLesson(LessonDto lessonDto, User user);

    LessonDto deleteLesson(long id);



}
