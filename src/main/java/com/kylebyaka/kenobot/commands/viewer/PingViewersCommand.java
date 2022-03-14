package com.kylebyaka.kenobot.commands.viewer;

import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.controllers.impl.viewer.ViewerController;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.ViewerService;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@Component
public class PingViewersCommand implements Command<ViewerController> {

    @Autowired
    private ViewerService viewerService;

    @Override
    public Response execute(String message, User user) {
        List<Viewer> allViewers = viewerService.findAll();
        return null;
    }

    @Override
    public String getName() {
        return "пинг";
    }

    @Override
    public String getType() {
        return null;
    }
}