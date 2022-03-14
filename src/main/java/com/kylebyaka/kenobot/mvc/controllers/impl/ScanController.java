package com.kylebyaka.kenobot.mvc.controllers.impl;

import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import com.kylebyaka.kenobot.mvc.services.ScanService;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

@Deprecated
//@Component
public class ScanController implements Command<CommandsListener> {
    private static final String CONTROLLER_NAME = "скан";

    @Autowired
    private ScanService scanService;

    public ScanController() {
        scanService = new ScanService();
    }

    @Override
    public Response execute(String message, User user) {
        scanService.globalScan();
        return Response.builder().message("Success!").build();
    }

    @Override
    public String getName() {
        return CONTROLLER_NAME;
    }

    @Override
    public String getType() {
        return null;
    }

}
