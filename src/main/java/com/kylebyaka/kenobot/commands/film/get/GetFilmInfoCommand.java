package com.kylebyaka.kenobot.commands.film.get;

import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.rest.ApiResponse;
import com.kylebyaka.kenobot.mvc.models.rest.FilmRestDTO;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GetFilmInfoCommand extends FindFilmTemplate<FilmController> {

    @Override
    protected Response processResult(Film film, User user) {
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
                    .message("Инфо: ")
                    .messageEmbed(film.buildEmbed(embed))
                    .build();
        }
    }

    @Override
    public String getName() {
        return "инфо";
    }

    @Override
    public String getType() {
        return Type.INPUT.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает инфо по названию фильма. Пример: " + getName() + " filmName";
    }
}
