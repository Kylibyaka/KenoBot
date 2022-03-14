package com.kylebyaka.kenobot.listeners;

import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import com.kylebyaka.kenobot.mvc.services.ViewerService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kylebyaka.kenobot.utils.BotUtil.sendLogMessage;

@Component
public class FilmThreadListener extends ListenerAdapter {

    @Autowired
    private FilmService filmService;

    @Autowired
    private ViewerService viewerService;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (isCorrectChannel(event)) {
            String filmName = event.getMessage().getContentDisplay().trim();
            filmService.addByNameAndViewer(event.getMessageIdLong(), filmName, viewerService.getOrCreateViewer(user));
            sendLogMessage(event.getJDA(), getMessage(filmName, user.getName(), " добавлен"));
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        String filmName = event.getMessage().getContentDisplay().trim();
        if (isCorrectChannel(event)) {
            processUpdate(event, filmName);
        }
    }

    private void processUpdate(@NotNull MessageUpdateEvent event, String filmName) {
        if (filmName.startsWith("~~")) {
            String film = filmName.replaceAll("~~", "");
            filmService.setViewed(film);
            sendLogMessage(event.getJDA(), getMessage(film, event.getAuthor().getName(), " отмечен просмотренным"));
        } else {
            filmService.update(event.getMessageIdLong(), filmName);
            sendLogMessage(event.getJDA(), "Название фильма: " + filmName + " изменено");
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (isCorrectChannel(event)) {
            Film film = filmService.findById(event.getMessageIdLong());
            filmService.delete(film);
            sendLogMessage(event.getJDA(), getMessage(film.getName(), film.getAddedBy().getName(), " удален"));
        }
    }

    private boolean isCorrectChannel(@NotNull GenericMessageEvent event) {
        return event.getChannel().getName().equals("кинцы");
    }

    private String getMessage(String filmName, String user, String action) {
        return "Фильм: " + filmName + " от " + user + action;
    }
}
