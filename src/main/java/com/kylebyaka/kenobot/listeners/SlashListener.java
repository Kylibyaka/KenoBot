package com.kylebyaka.kenobot.listeners;

import com.kylebyaka.kenobot.adapters.JDACommandAdapter;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlashListener extends ListenerAdapter {

    @Autowired
    private JDACommandAdapter adapter;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getInteraction().getUser();
        String name = user.getName();
        Response response = adapter.getController(name, "").execute("", user);
        event.getInteraction().reply(response.createNewMessage()).setEphemeral(true).queue();
    }
}
