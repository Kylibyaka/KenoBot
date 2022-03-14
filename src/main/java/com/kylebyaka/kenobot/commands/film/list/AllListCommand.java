package com.kylebyaka.kenobot.commands.film.list;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AllListCommand implements Command<FilmController> {

    @Autowired
    private FilmService filmService;

    @Override
    public Response execute(String command, User user) {
        int pageInt = getPageNumber(command);
        EmbedBuilder builder = new EmbedBuilder().setColor(Color.orange);
        fillWithValues(pageInt, builder);
        builder.setFooter("Страница: " + pageInt);
        ActionRow of = createButtons(pageInt);
        return Response.builder()
                .messageEmbed(builder.build())
                .components(Collections.singletonList(of))
                .build();
    }

    private int getPageNumber(String command) {
        String[] s = command.split(" ");
        String page = s[s.length - 1];
        int pageInt;
        if (page.isEmpty()) {
            pageInt = 0;
        } else {
            pageInt = Integer.parseInt(page);
        }
        return pageInt;
    }

    private void fillWithValues(int pageInt, EmbedBuilder builder) {
        List<Film> all = filmService.findAllNotViewedPageable(PageRequest.of(pageInt, 20));
        if (all.isEmpty()) {
            builder.addField("Все фильмы", "-", false);
        } else {
            String message = joinFilms(all);
            builder.addField("Все фильмы", message, false);
        }
    }

    @NotNull
    private ActionRow createButtons(int pageInt) {
        return ActionRow.of(Button.primary("pagination:кено список " + (pageInt - 1), Emoji.fromUnicode("U+25C0")),
                Button.primary("pagination:кено список " + (pageInt + 1), Emoji.fromUnicode("U+25B6")));
    }

    private String joinFilms(List<Film> all) {
        return all.stream()
                .map(Film::getName)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getName() {
        return "список";
    }

    @Override
    public String getType() {
        return Type.ACTION.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает список всех фильмов";
    }
}
