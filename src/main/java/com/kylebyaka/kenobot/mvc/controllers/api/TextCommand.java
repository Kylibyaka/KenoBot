package com.kylebyaka.kenobot.mvc.controllers.api;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public interface TextCommand<T> extends Command<T> {
    /***
     * @param message name of a controller to delegate processing
     * @param user
     * @param channel
     *
     */
    void execute(String message, User user, MessageChannel channel);
}
