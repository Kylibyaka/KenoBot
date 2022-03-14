package com.kylebyaka.kenobot.commands.viewer;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.viewer.ViewerController;
import com.kylebyaka.kenobot.mvc.exceptions.db.ViewerNotFoundException;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.FilmService;
import com.kylebyaka.kenobot.mvc.services.ViewerService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GetUserVotesCommand implements Command<ViewerController> {

    @Autowired
    private ViewerService viewerService;

    @Autowired
    private FilmService filmService;

    @Override
    public Response execute(String command, User user) {
        Viewer byName;
        if (command == null || command.isBlank()) {
            String name = user.getName();
            Optional<Viewer> viewer = viewerService.findByName(name);

            byName = viewer.orElseThrow(() -> getViewerNotFoundException(name));
        } else {
            if (command.startsWith("@")) {
                command = command.replace("@", "");
            }
            String finalCommand = command;
            byName = viewerService.findByName(command).orElseThrow(() -> getViewerNotFoundException(finalCommand));
        }
        List<Film> allByViewerVotes = filmService.findAllByViewerVotesNotViewed(byName);
        List<Film> allByViewerAdded = filmService.findViewerAddedNotViewed(byName);
        MessageEmbed messageEmbed = new EmbedBuilder()
                .setColor(Color.orange)
                .setTitle(byName.getName())
                .addField("Добавил", formList(allByViewerAdded), true)
                .addField("Проголосовал", formList(allByViewerVotes), true)
                .build();
        return Response.builder()
                .messageEmbed(messageEmbed)
                .build();
    }

    @NotNull
    private static String formList(List<Film> allByViewerVotes) {
        return allByViewerVotes
                .stream()
                .map(Film::getName)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getName() {
        return "голоса";
    }

    @Override
    public String getType() {
        return Type.INPUT.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает твои голоса или голоса другого пользователя. Пример: " + getName() + " или " + getName() + " username";
    }

    private ViewerNotFoundException getViewerNotFoundException(String name) {
        return new ViewerNotFoundException(String.format(ViewerNotFoundException.VIEWER_NOT_FOUND_IN_DATABASE, name));
    }
}
