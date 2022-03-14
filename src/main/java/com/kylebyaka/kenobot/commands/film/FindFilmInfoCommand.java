package com.kylebyaka.kenobot.commands.film;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.rest.ApiResponse;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FindFilmInfoCommand implements Command<FilmController> {

    @Autowired
    private FilmService filmService;

    @Override
    public Response execute(String message, User user) {
        ResponseEntity<ApiResponse> response = filmService.makeFindRequest(message);

        ApiResponse body = response.getBody();
        if (body == null) {
            return Response.builder().message("Проблемы с запросом.").build();
        }

        if (body.getFilms().isEmpty()) {
            return Response.builder().message("Поиск не дал результатов.").build();
        } else {
            return Response.builder()
                    .message("Поиск:")
                    .messageEmbed(body.getFilms().get(0).getEmbed().build())
                    .build();
        }
    }


    @Override
    public String getName() {
        return "поиск";
    }

    @Override
    public String getType() {
        return Type.INPUT.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает информацию по ключевым словам с кинопоиска";
    }
}
