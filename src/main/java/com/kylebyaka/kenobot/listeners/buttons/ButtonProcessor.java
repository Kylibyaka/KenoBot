package com.kylebyaka.kenobot.listeners.buttons;

import com.kylebyaka.kenobot.adapters.JDACommandAdapter;
import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ButtonProcessor {

    @Autowired
    protected JDACommandAdapter adapter;

    public abstract void process(@NotNull ButtonInteractionEvent event, Button button);

    public abstract Command.Type getType();
}
