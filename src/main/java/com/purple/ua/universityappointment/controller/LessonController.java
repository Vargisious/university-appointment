package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.service.LessonService;
import com.purple.ua.universityappointment.util.LessonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.purple.ua.universityappointment.util.LessonMapper.INSTANCE;

@RestController
@RequestMapping("lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping("/create")
    ResponseEntity<LessonDto> createLesson(@Valid @RequestBody LessonDto lessonDto) {
        lessonService.createLesson(INSTANCE.toEntity(lessonDto));
        return new ResponseEntity(lessonDto, HttpStatus.CREATED);
    }
}
