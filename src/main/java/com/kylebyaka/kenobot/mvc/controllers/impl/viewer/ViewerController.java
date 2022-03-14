package com.kylebyaka.kenobot.mvc.controllers.impl.viewer;

import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.controllers.api.NestedCommand;
import org.springframework.stereotype.Component;

@Component
public class ViewerController extends NestedCommand<CommandsListener, ViewerController> {

    private static final String CONTROLLER_NAME = "зритель";

    @Override
    public String getName() {
        return CONTROLLER_NAME;
    }
}
