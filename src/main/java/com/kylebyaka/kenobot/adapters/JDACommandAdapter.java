package com.kylebyaka.kenobot.adapters;

import com.kylebyaka.kenobot.adapters.models.CommandsContainer;
import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JDACommandAdapter {

    @Autowired
    private CommandsContainer container;

    public String getCommand(MessageReceivedEvent event) {
        return container.getCommandFromMessage(event.getAuthor().getName(), event.getMessage().getContentDisplay());
    }

    public Command<CommandsListener> getController(String name, String message) {
        return container.getController(name, message);
    }
    
    public Command<CommandsListener> getController(String controllerName) {
        return container.getController(controllerName);
    }

    public String getCommand(ButtonInteractionEvent event) {
        return event.getButton().getLabel();
    }

    public boolean isCached(String user) {
        return container.isCached(user);
    }
}
