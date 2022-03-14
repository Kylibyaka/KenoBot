package com.kylebyaka.kenobot.listeners.buttons;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class IntermediateButtonProcessor extends ButtonProcessor {
    
    @Override
    public void process(@NotNull ButtonInteractionEvent event, Button button) {
        String command = adapter.getCommand(event);
        Response response = adapter.getController(event.getInteraction().getUser().getName(), button.getLabel()).execute(command, event.getUser());
        event.editMessage(response.createEditMessage()).queue();
    }

    @Override
    public Command.Type getType() {
        return Command.Type.INTERMEDIATE;
    }
}
