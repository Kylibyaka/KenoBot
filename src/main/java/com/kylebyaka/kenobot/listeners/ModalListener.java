package com.kylebyaka.kenobot.listeners;

import com.kylebyaka.kenobot.adapters.JDACommandAdapter;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModalListener extends ListenerAdapter {
    @Autowired
    private JDACommandAdapter adapter;

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String command = event.getModalId();
        String input = event.getValue("input").getAsString();
        Response response = adapter.getController(event.getUser().getName(), command).execute(command + " " + input, event.getUser());
        event.reply(response.createNewMessage()).queue();
    }
}
