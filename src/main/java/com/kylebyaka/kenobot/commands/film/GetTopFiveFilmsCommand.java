package com.kylebyaka.kenobot.commands.film;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
public class GetTopFiveFilmsCommand implements Command<FilmController> {

    @Autowired
    private FilmService filmService;

    @Override
    public Response execute(String message, User user) {
        List<String> topFiveFilms = filmService.findTopFiveFilms();
        MessageEmbed messageEmbed = new EmbedBuilder()
                .setColor(Color.orange)
                .addField("Топ 5 фильмов", String.join("\n", topFiveFilms), false)
                .build();
        return Response.builder()
                .messageEmbed(messageEmbed)
                .build();
    }

    @Override
    public String getName() {
        return "топ";
    }

    @Override
    public String getType() {
        return Type.ACTION.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает топ 5 фильмов по голосам.";
    }
}
