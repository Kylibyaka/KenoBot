package com.kylebyaka.kenobot.mvc.controllers.api;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonCommand<T> extends Command<T> {

    void execute(String message, ButtonInteractionEvent event);
}
