package com.purple.ua.universityappointment.service;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.model.User;

import java.util.List;

public interface LessonService {

    LessonDto getLessonById(long id);

    List<LessonDto> getAllLesson(User user);

    List<LessonDto> getLessonsByLecturerFirstNameAndLastName(String firstName, String secondName);

    List<LessonDto> getLessonsByLecturerId(long id);

    List<LessonDto> getLessonByFieldOfStudy(String name);

    LessonDto createLesson(LessonDto lessonDto, User user);

    LessonDto updateLesson(LessonDto lessonDto);

    LessonDto deleteLesson(long id);



}
