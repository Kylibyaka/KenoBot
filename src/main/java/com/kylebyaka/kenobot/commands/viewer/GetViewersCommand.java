package com.kylebyaka.kenobot.commands.viewer;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.viewer.ViewerController;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.ViewerService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetViewersCommand implements Command<ViewerController> {

    @Autowired
    private ViewerService viewerService;

    @Override
    public Response execute(String command, User user) {
        List<Viewer> all = viewerService.findAll();
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.orange);
        if (all.isEmpty()) {
            builder.addField("Зрители", "-", false);
        } else {
            String viewerNames = all.stream().map(Viewer::getName).collect(Collectors.joining("\n"));
            builder.addField("Зрители", viewerNames, false);
        }
        return Response.builder().messageEmbed(builder.build()).build();
    }

    @Override
    public String getName() {
        return "все";
    }

    @Override
    public String getType() {
        return Type.ACTION.getName();
    }

    @Override
    public String getDescription() {
        return "Возвращает список всех зрителей";
    }
}
