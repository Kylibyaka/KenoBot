package com.kylebyaka.kenobot.mvc.exceptions.db;

public class FilmNotFoundException extends DatabaseException {

    public static final String FILM_NOT_FOUND_IN_DATABASE = "Film %s not found in database";

    public FilmNotFoundException(String message) {
        super(message);
    }
}
