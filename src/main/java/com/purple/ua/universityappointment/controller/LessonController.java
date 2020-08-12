package com.purple.ua.universityappointment.controller;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.security.MyUserDetails;
import com.purple.ua.universityappointment.service.LessonService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @GetMapping("/all")
    ResponseEntity<List<LessonDto>> getAllRelatedLessons(@AuthenticationPrincipal MyUserDetails userDetails) {
        List<LessonDto> lessons = lessonService.getAllLesson(userDetails.getUser());
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<LessonDto> getLessonById(@RequestParam long lessonId) {
        LessonDto lesson = lessonService.getLesson(lessonId);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @GetMapping("/lecturer")
    ResponseEntity<List<LessonDto>> getLessonsByLecturerId(@RequestParam long lecturerId) {
        List<LessonDto> lessons = lessonService.getLessonsByLecturerId(lecturerId);
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
    ResponseEntity<LessonDto> updateLesson(@RequestBody LessonDto lessonDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        LessonDto lesson = lessonService.updateLesson(lessonDto, userDetails.getUser());
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @DeleteMapping("/delete")
    ResponseEntity<LessonDto> deleteLesson(@RequestParam long id) {
        LessonDto lesson = lessonService.deleteLesson(id);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }
}

