package com.kylebyaka.kenobot.listeners;

import com.kylebyaka.kenobot.adapters.JDACommandAdapter;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.utils.BotUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * Main enter point for interacting with bot
 * All messages without @Kenobot will be ignored
 */
@Component
public class CommandsListener extends ListenerAdapter {

    @Autowired
    private JDACommandAdapter adapter;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        BotUtil.logMessage(event);
        Message message = event.getMessage();

        if (isNotForBot(event)) return;

        String command = adapter.getCommand(event);
        Response response = adapter.getController(message.getAuthor().getName(), message.getContentDisplay()).execute(command, event.getAuthor());
        event.getMessage().reply(response.createNewMessage()).queue();
    }

    private boolean isNotForBot(MessageReceivedEvent event) {
        return !event.getMessage().getContentDisplay().startsWith("!кенобот");
    }
}
