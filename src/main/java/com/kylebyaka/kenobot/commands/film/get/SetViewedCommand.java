package com.kylebyaka.kenobot.commands.film.get;

import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

@Component
public class SetViewedCommand extends FindFilmTemplate<FilmController> {

    @Override
    protected Response processResult(Film byName, User user) {
        if (byName.isViewed()) {
            return Response.builder().message(byName.getName() + " уже отмечен просмотренным.").build();
        }
        String jumpUrl = byName.getJumpUrl();
        return Response.builder()
                .message("<@" + byName.getAddedBy().getViewerId() + "> Вычеркни фильм из списка (поставив ~~ в начале и конце сообщения) " + jumpUrl)
                .build();
    }

    @Override
    public String getName() {
        return "посмотрели";
    }

    @Override
    public String getType() {
        return Type.INPUT.getName();
    }

    @Override
    public String getDescription() {
        return "Помогает отметить фильм просмотренным.";
    }
}
