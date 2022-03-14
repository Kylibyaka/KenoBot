package com.kylebyaka.kenobot.listeners.buttons;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class InputButtonProcessor extends ButtonProcessor {
    @Override
    public void process(@NotNull ButtonInteractionEvent event, Button button) {
        String command = adapter.getCommand(event);
        event.replyModal(createModal(command)).queue();
    }

    @Override
    public Command.Type getType() {
        return Command.Type.INPUT;
    }

    private Modal createModal(String command) {
        TextInput subject = TextInput.create("input", "Параметр", TextInputStyle.SHORT)
                .setPlaceholder("Введи данные для команды").setMaxLength(100).setRequired(false)
                .build();
        return Modal.create(command, command).addActionRows(ActionRow.of(subject)).build();
    }
}
