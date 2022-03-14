package com.kylebyaka.kenobot.mvc.controllers.impl.film;

import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.controllers.api.NestedCommand;
import org.springframework.stereotype.Component;

@Component
public class FilmController extends NestedCommand<CommandsListener, FilmController> {

    private static final String CONTROLLER_NAME = "кено";

    @Override
    public String getName() {
        return CONTROLLER_NAME;
    }
}
