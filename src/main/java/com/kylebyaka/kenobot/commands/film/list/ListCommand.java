package com.kylebyaka.kenobot.commands.film.list;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.film.FilmController;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

//@Component
public class ListCommand implements Command<FilmController> {

    @Override
    public Response execute(String message, User user) {
        return null;
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
        return "Возвращает список фильмов";
    }
}
