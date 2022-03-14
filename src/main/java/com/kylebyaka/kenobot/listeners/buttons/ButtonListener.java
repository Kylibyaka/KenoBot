package com.kylebyaka.kenobot.listeners.buttons;

import com.kylebyaka.kenobot.adapters.JDACommandAdapter;
import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ButtonListener extends ListenerAdapter {

    private EnumMap<Command.Type, ButtonProcessor> processorTypeMap;

    @Autowired
    public void populateMap(List<ButtonProcessor> processors) {
        processorTypeMap = processors.stream()
                .collect(Collectors.toMap(ButtonProcessor::getType,
                        Function.identity(),
                        (o, o2) -> o,
                        () -> new EnumMap<>(Command.Type.class)));
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Button button = event.getButton();
        String id = button.getId();
        String[] split = id.split(":");
        processorTypeMap.get(Command.Type.valueOf(split[0].toUpperCase())).process(event, button);
    }
}
