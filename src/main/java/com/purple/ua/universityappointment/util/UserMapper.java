package com.purple.ua.universityappointment.util;

import com.purple.ua.universityappointment.dto.UserDto;
import com.purple.ua.universityappointment.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User userEntity);

    List<UserDto> entitiesToDto(List<User> userEntities);

    User dtoToEntity(UserDto userDto);
}
