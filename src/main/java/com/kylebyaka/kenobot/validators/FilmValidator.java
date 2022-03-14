package com.kylebyaka.kenobot.validators;

import com.kylebyaka.kenobot.mvc.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class FilmValidator {

    public void validateFilmName(String filmName) {
        if (filmName == null || filmName.isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
    }
}
