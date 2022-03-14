package com.kylebyaka.kenobot.listeners.buttons;

import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class PaginationButtonProcessor extends ButtonProcessor {
    @Override
    public void process(@NotNull ButtonInteractionEvent event, Button button) {
        String command = event.getButton().getId().split(":")[1].trim();
        Command<CommandsListener> controller = adapter.getController(command.split(" ")[0]);
        String[] s = command.split(" ");
        command = Arrays.stream(s).skip(1).collect(Collectors.joining(" "));
        Response response = controller.execute(command, event.getUser());
        event.editMessage(response.createEditMessage()).queue();
    }

    @Override
    public Command.Type getType() {
        return Command.Type.PAGINATION;
    }
}
