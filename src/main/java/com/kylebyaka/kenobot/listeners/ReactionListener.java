package com.kylebyaka.kenobot.listeners;

import com.kylebyaka.kenobot.mvc.services.FilmService;
import com.kylebyaka.kenobot.utils.BotUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.BiPredicate;

import static com.kylebyaka.kenobot.utils.BotUtil.sendLogMessage;

@Component
public class ReactionListener extends ListenerAdapter {

    @Autowired
    private FilmService filmService;

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        precessReaction(event, (filmName, user) -> filmService.addVote(filmName, user), " добавлен");
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        precessReaction(event, (filmName, user) -> filmService.removeVote(filmName, user), " удален");
    }

    private void precessReaction(@NotNull GenericMessageReactionEvent event, BiPredicate<String, User> predicate, String suffix) {
        ThreadChannel kinci = BotUtil.findThreadChannel(event.getJDA(), "кинцы");
        Message message = event.retrieveMessage().complete();
        if (isRightEmoteAndChannel(event.getReaction(), event.getChannel(), kinci)) {
            String filmName = message.getContentDisplay().trim();
            User user = event.retrieveUser().complete();
            if (predicate.test(filmName, user)) {
                sendLogMessage(event.getJDA(), "Голос за " + filmName + " от " + user.getName() + suffix);
            }
        }
    }

    private boolean isRightEmoteAndChannel(MessageReaction event, MessageChannel channel, ThreadChannel kinci) {
        return event.getEmoji().getAsReactionCode().equals("☑️") && channel.getId().equals(kinci.getId());
    }
}
