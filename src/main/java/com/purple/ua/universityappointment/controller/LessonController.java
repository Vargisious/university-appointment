package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.LessonService;
import com.purple.ua.universityappointment.util.CycleAvoidingMappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.purple.ua.universityappointment.util.LessonMapper.INSTANCE;

@RestController
@RequestMapping("lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @GetMapping("/all")
    ResponseEntity<List<Lesson>> getAllLessons(@AuthenticationPrincipal MyUserDetails userDetails) {
        List<Lesson> allLessons = lessonService.getAllLesson(userDetails.getUser());
        return new ResponseEntity(allLessons, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<LessonDto> getLessonById(@RequestParam long id) {
        Lesson lesson = lessonService.getLessonById(id);
        return new ResponseEntity<>(INSTANCE.toDto(lesson, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/lecturer")
    ResponseEntity<List<LessonDto>> getLessonsByLecturerId(@RequestParam long id) {
        List<Lesson> lessons = lessonService.getLessonsByLecturerId(id);
        return new ResponseEntity<>(INSTANCE.listToDto(lessons, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @GetMapping("/lecturer/name")
    ResponseEntity<List<LessonDto>> getLessonsByLecturerFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        List<Lesson> lessons = lessonService.getLessonsByLecturerFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(INSTANCE.listToDto(lessons, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PostMapping("/create")
    ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        Lesson lesson = INSTANCE.toEntity(lessonDto);
        lessonService.createLesson(lesson, userDetails.getUser());
        return new ResponseEntity(INSTANCE.toDto(lesson, new CycleAvoidingMappingContext()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PutMapping("/update")
    ResponseEntity<LessonDto> updateLesson(@RequestBody LessonDto lessonDto) {
        Lesson lesson = lessonService.updateLesson(INSTANCE.toEntity(lessonDto));
        return new ResponseEntity<>(INSTANCE.toDto(lesson, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @DeleteMapping("/delete")
    ResponseEntity<LessonDto> deleteLesson(@RequestParam long id) {
        Lesson lesson = lessonService.deleteLesson(id);
        return new ResponseEntity<>(INSTANCE.toDto(lesson, new CycleAvoidingMappingContext()), HttpStatus.OK);
    }
}

