package com.purple.ua.universityappointment.util;

import com.purple.ua.universityappointment.dto.LessonDto;
import com.purple.ua.universityappointment.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LessonMapper {

    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    Lesson toEntity(LessonDto lessonDto);

    List<LessonDto> listToDto(List<Lesson> lessons);

    LessonDto toDto(Lesson lesson);
}
