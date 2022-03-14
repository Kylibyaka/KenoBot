package com.kylebyaka.kenobot.mvc.models.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Getter
@AllArgsConstructor
@Builder
public class Response {

    private String message;
    private Collection<LayoutComponent> components;
    private MessageEmbed messageEmbed;

    @NotNull
    public MessageCreateData createNewMessage() {
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        if (this.getMessage() != null) {
            messageCreateBuilder.addContent(this.getMessage());
        }
        if (this.getComponents() != null) {
            messageCreateBuilder.addComponents(this.getComponents());
        }
        if (this.getMessageEmbed() != null) {
            messageCreateBuilder.setEmbeds(this.getMessageEmbed());
        }
        return messageCreateBuilder.build();
    }

    @NotNull
    public MessageEditData createEditMessage() {
        MessageEditBuilder messageEditBuilder = new MessageEditBuilder();
        if (this.getMessage() != null) {
            messageEditBuilder.setContent(this.getMessage());
        }
        if (this.getComponents() != null) {
            messageEditBuilder.setComponents(this.getComponents());
        }
        if (this.getMessageEmbed() != null) {
            messageEditBuilder.setEmbeds(this.getMessageEmbed());
        }
        return messageEditBuilder.build();
    }
}
