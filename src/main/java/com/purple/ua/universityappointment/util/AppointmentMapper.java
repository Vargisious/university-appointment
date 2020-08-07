package com.purple.ua.universityappointment.util;



import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.model.Appointment;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    Appointment toEntity(AppointmentDto appointmentDto);

    List<AppointmentDto> listToDto(List<Appointment> appointments, @Context CycleAvoidingMappingContext context);

    AppointmentDto toDto(Appointment appointment,@Context CycleAvoidingMappingContext context);

}
