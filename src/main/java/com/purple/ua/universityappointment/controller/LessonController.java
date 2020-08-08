package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.LessonService;
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

@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @GetMapping("/all")
    ResponseEntity<List<LessonDto>> getAllLessons(@AuthenticationPrincipal MyUserDetails userDetails) {
        List<LessonDto> lessons = lessonService.getAllLesson(userDetails.getUser());
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<LessonDto> getLessonById(@RequestParam long id) {
        LessonDto lesson = lessonService.getLessonById(id);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping("/lecturer")
    ResponseEntity<List<LessonDto>> getLessonsByLecturerId(@RequestParam long id) {
        List<LessonDto> lessons = lessonService.getLessonsByLecturerId(id);
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping("/lecturer/name")
    ResponseEntity<List<LessonDto>> getLessonsByLecturerFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        List<LessonDto> lessons = lessonService.getLessonsByLecturerFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PostMapping("/create")
    ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        LessonDto lesson = lessonService.createLesson(lessonDto, userDetails.getUser());
        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PutMapping("/update")
    ResponseEntity<LessonDto> updateLesson(@RequestBody LessonDto lessonDto) {
        LessonDto lesson = lessonService.updateLesson(lessonDto);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @DeleteMapping("/delete")
    ResponseEntity<LessonDto> deleteLesson(@RequestParam long id) {
        LessonDto lesson = lessonService.deleteLesson(id);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }
}

