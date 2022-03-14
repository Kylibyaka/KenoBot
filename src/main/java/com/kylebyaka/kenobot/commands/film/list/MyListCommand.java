package com.kylebyaka.kenobot.commands.film.list;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.exceptions.db.ViewerNotFoundException;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import com.kylebyaka.kenobot.mvc.services.ViewerService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.kylebyaka.kenobot.mvc.exceptions.db.ViewerNotFoundException.VIEWER_NOT_FOUND_IN_DATABASE;

@Component
public class MyListCommand implements Command<ListCommand> {

    @Autowired
    private FilmService filmService;

    @Autowired
    private ViewerService viewerService;

    @Override
    public Response execute(String command, User user) {
        Viewer viewer = viewerService.findById(user.getIdLong()).orElseThrow(() -> new ViewerNotFoundException(String.format(VIEWER_NOT_FOUND_IN_DATABASE, user.getName())));
        List<Film> my = filmService.findViewerAddedNotViewed(viewer);
        EmbedBuilder builder = new EmbedBuilder().setColor(Color.orange);
        if (my.isEmpty()) {
            builder.addField("Фильмы от " + user.getName(), "-", false);
        } else {
            String message = joinFilms(my);
            builder.addField("Фильмы от " + user.getName(), message, false);
        }
        return Response.builder().messageEmbed(builder.build()).build();
    }

    private String joinFilms(List<Film> all) {
        return all.stream().map(Film::getName).collect(Collectors.joining("\n"));
    }

    @Override
    public String getName() {
        return "мой";
    }

    @Override
    public String getType() {
        return Type.ACTION.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает список фильмов, которые ты добавил";
    }
}
