package com.kylebyaka.kenobot.commands.film.get;

import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.stream.Collectors;

@Component
public class GetVotesCommand extends FindFilmTemplate<FilmController> {

    @Override
    protected Response processResult(Film byName, User user) {
        String votersName = getVotersName(byName);
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.orange)
                .setTitle(byName.getName());
        String s;
        if (votersName.isEmpty()) {
            s = "-";
        } else {
            s = votersName;
        }
        embed.addField("Проголосовали", s, false);
        return Response.builder().messageEmbed(embed.build()).build();
    }

    @Override
    public String getName() {
        return "голоса";
    }

    @Override
    public String getType() {
        return Type.INPUT.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает список проголосовавших за фильм. Пример: " + getName() + " filmName";
    }

    @NotNull
    private String getVotersName(Film byName) {
        return byName.getVotes()
                .stream()
                .map(Viewer::getName)
                .collect(Collectors.joining("\n"));
    }
}
