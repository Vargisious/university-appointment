package com.purple.ua.universityappointment.util;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.model.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User userEntity, @Context CycleAvoidingMappingContext context);

    List<UserDto> listToDto(List<User> userEntities, @Context CycleAvoidingMappingContext context);

    User toEntity(UserDto userDto);
}
