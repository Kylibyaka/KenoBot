package com.kylebyaka.kenobot.commands.film;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.rest.ApiResponse;
import com.kylebyaka.kenobot.mvc.models.rest.FilmRestDTO;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class RandomFilmCommand implements Command<FilmController> {

    @Autowired
    private FilmService filmService;

    @Override
    public Response execute(String command, User user) {
        List<Film> allNotViewed = filmService.findAllNotViewed();
        if (allNotViewed.isEmpty()) {
            return Response.builder().message("Нет фильмов :(").build();
        }
        Film film = allNotViewed.get(new Random().nextInt(allNotViewed.size() - 1));
        return getResponse(film);
    }

    private Response getResponse(Film film) {
        ResponseEntity<ApiResponse> response = filmService.makeFindRequest(film.getName());

        ApiResponse body = response.getBody();
        if (body == null || body.getFilms().isEmpty()) {
            return Response.builder().messageEmbed(film.getEmber()).build();
        } else {
            FilmRestDTO filmRestDTO = body.getFilms().get(0);
            EmbedBuilder embed = filmRestDTO.getEmbed();
            return Response.builder()
                    .message("Смотри:")
                    .messageEmbed(film.buildEmbed(embed))
                    .build();
        }
    }

    @Override
    public String getName() {
        return "рандом";
    }

    @Override
    public String getType() {
        return Type.ACTION.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает случайный фильм из ещё не просмотренных";
    }
}
