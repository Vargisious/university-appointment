package com.purple.ua.universityappointment.util;

import com.purple.ua.universityappointment.dto.AppointmentDto;
import com.purple.ua.universityappointment.model.Lesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.lang.Math.round;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PriceUtils {

    public static AppointmentDto countPrice(Lesson lesson, long duration, AppointmentDto appointmentDto) {
        double perMinute = (double) lesson.getPrice() / 60;
        double price = duration * perMinute;
        if (duration > lesson.getDiscountStart()) {
            appointmentDto.setFinalPrice(round(price - price * lesson.getDiscount()));
        } else {
            appointmentDto.setFinalPrice(price);
        }
        return appointmentDto;
    }

}
