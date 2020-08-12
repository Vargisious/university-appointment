package com.purple.ua.universityappointment.service.impl;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.exception.LessonNotFoundException;
import com.purple.ua.universityappointment.exception.TimeConflictException;
import com.purple.ua.universityappointment.model.Lesson;
import com.purple.ua.universityappointment.model.User;
import com.purple.ua.universityappointment.repository.LessonRepository;
import com.purple.ua.universityappointment.service.LessonService;
import com.purple.ua.universityappointment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.purple.ua.universityappointment.util.mapper.LessonMapper.INSTANCE;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final UserService userService;

    @Override
    public LessonDto getLesson(long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return INSTANCE.toDto(lesson.orElseThrow(() ->
                new LessonNotFoundException(HttpStatus.NOT_FOUND, "Lesson with id: " + id + " not found")));
    }

    @Override
    public Lesson getLessonById(long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.orElseThrow(() ->
                new LessonNotFoundException(HttpStatus.NOT_FOUND, "Lesson with id: " + id + " not found"));
    }

    @Override
    public List<LessonDto> getAllLesson(User user) {
        List<Lesson> lessons = lessonRepository.findByLecturer(user);
        if (!lessons.isEmpty()) {
            return INSTANCE.listToDto(lessons);
        } else {
            throw new LessonNotFoundException(HttpStatus.NOT_FOUND, "No lessons found");
        }
    }

    @Override
    public List<LessonDto> getLessonsByLecturerId(long id) {
        List<Lesson> lessons = lessonRepository.findByLecturerId(id);
        if (!lessons.isEmpty()) {
            return INSTANCE.listToDto(lessons);
        } else {
            throw new LessonNotFoundException(HttpStatus.NOT_FOUND, "Lesson with lecturer id: " + id + " not found");
        }
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto, User user) {
        LocalDateTime fromDate = lessonDto.getFromDate();
        LocalDateTime toDate = lessonDto.getToDate();
        if (!toDate.isAfter(fromDate)) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "DateFrom is after ToDate");
        }
        if (lessonRepository.findTimeOverlap(fromDate, toDate, user.getId()).isEmpty()) {
            User lecturer = userService.getUserById(user.getId());
            Lesson lesson = INSTANCE.toEntity(lessonDto);
            lesson.setLecturer(lecturer);
            return INSTANCE.toDto(lessonRepository.save(lesson));
        } else {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date is overlapping another lesson");
        }
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto, User user) {
        LocalDateTime fromDate = lessonDto.getFromDate();
        LocalDateTime toDate = lessonDto.getToDate();
        if (!toDate.isAfter(fromDate)) {
            throw new TimeConflictException(HttpStatus.CONFLICT, "DateFrom is after ToDate");
        }
        if (lessonRepository.findTimeOverlapExclude(fromDate, toDate, user.getId(),
                lessonDto.getId()).isEmpty()) {
            lessonRepository.save(INSTANCE.toEntity(lessonDto));
            return lessonDto;
        } else {
            throw new TimeConflictException(HttpStatus.CONFLICT, "The date is overlapping another lesson");
        }
    }

    @Override
    public LessonDto deleteLesson(long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() ->
                new LessonNotFoundException(HttpStatus.NOT_FOUND, "Lesson with id: " + id + " not found"));
        lessonRepository.delete(lesson);
        return INSTANCE.toDto(lesson);
    }
}

