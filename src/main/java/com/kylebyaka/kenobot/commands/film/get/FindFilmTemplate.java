package com.kylebyaka.kenobot.commands.film.get;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import com.kylebyaka.kenobot.validators.FilmValidator;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * If command requires finding film by name you can use this template class
 * @param <T>
 */
@Component
public abstract class FindFilmTemplate<T> implements Command<T> {

    @Autowired
    protected FilmService filmService;

    @Autowired
    private FilmValidator validator;

    @Override
    public Response execute(String filmName, User user) {
        validator.validateFilmName(filmName);

        Optional<Film> byName = filmService.findByName(filmName);

        if (byName.isEmpty()) {
            List<Film> allByFuzzyName = filmService.findAllByFuzzyName(filmName);
            if (allByFuzzyName.isEmpty()) {
                return Response.builder().message("Фильм не найден").build();
            } else if (allByFuzzyName.size() > 1) {
                String allFilms = allByFuzzyName.stream().map(Film::getName).collect(Collectors.joining(", "));
                return Response.builder().message("Уточни название: " + allFilms).build();
            } else {
                byName = Optional.of(allByFuzzyName.get(0));
            }
        }
        return processResult(byName.get(), user);
    }

    protected abstract Response processResult(Film byName, User user);

    /*
     * Возможные ситуации:
     * 1. Фильм найден по точному имени
     * 2. Один фильм найден по неточному имени
     * 3. Несколько фильмов найдено по неточному имени
     * 4. Фильм не найден
     * */
    /*protected Optional<Film> getFilm(String filmName) {
        Optional<Film> byName = filmService.findByName(filmName);

        if (byName.isEmpty()) {
            List<Film> allByFuzzyName = filmService.findAllByFuzzyName(filmName);
            if (allByFuzzyName.isEmpty()) {
                executor.sendMessage("Фильм не найден");
                return Optional.empty();
            } else if (allByFuzzyName.size() > 1) {
                String allFilms = allByFuzzyName.stream().map(Film::getName).collect(Collectors.joining(", "));
                executor.sendMessage("Уточни название: " + allFilms);
                return Optional.empty();
            } else {
                return Optional.of(allByFuzzyName.get(0));
            }
        }
        return byName;
    }*/
}
