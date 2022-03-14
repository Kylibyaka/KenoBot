package com.kylebyaka.kenobot.adapters.models;

import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.controllers.api.Command;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kylebyaka.kenobot.mvc.controllers.api.Command.getDefaultCommand;
import static com.kylebyaka.kenobot.utils.BotUtil.FIRST_WORD_FROM_STRING;

@Component
public class CommandsContainer {

    private static final String APPEAL_REGEX = "^([!а-яА-Я0-9]+\\s*)";
    private static final String ACTION_REGEX = "([а-яА-Я0-9]+\\s*)";
    private Map<String, Command<CommandsListener>> mappings;
    /***
     * Simple cache for previous commands per user
     * key - username
     * value - controller name
     */
    private final Map<String, String> userActionMap = new HashMap<>();

    @Autowired
    public void populateMappings(List<Command<CommandsListener>> commands) {
        mappings = commands.stream().collect(Collectors.toMap(Command::getName, Function.identity()));
    }

    public String getCommandFromMessage(String name, String message) {
        String command;
        if (userActionMap.get(name) != null) {
            command = removeAppeal(message);
        } else {
            command = removeAppealAndAction(message);
        }
        return command;
    }

    public Command<CommandsListener> getController(String controllerName) {
        return mappings.get(controllerName);
    }

    public Command<CommandsListener> getController(String author, String message) {
        checkClearCommand(author, message);

        Command<CommandsListener> cachedCommand = mappings.get(userActionMap.get(author));
        if (cachedCommand != null) {
            return cachedCommand;
        }

        Optional<Command<CommandsListener>> controller = getAndCacheCommandFromMessage(author, message);
        return controller.orElse(getDefaultCommand(mappings.keySet()));
    }

    private void checkClearCommand(String author, String message) {
        Pattern compile = Pattern.compile(FIRST_WORD_FROM_STRING);
        message = checkAndRemoveAppeal(message);
        Matcher commandMatcher = compile.matcher(message);
        if (commandMatcher.find()) {
            checkClearCommand(commandMatcher, author);
        }
    }

    private void checkClearCommand(Matcher matcher, String username) {
        String group = matcher.group();
        if (group.equals("сброс")) {
            clearCache(username);
        }
    }

    private void clearCache(String username) {
        String cache = userActionMap.get(username);
        if (cache != null) {
            mappings.get(cache).clear(username);
            userActionMap.remove(username);
        }
    }

    private Optional<Command<CommandsListener>> getAndCacheCommandFromMessage(String username, String message) {
        String msg = checkAndRemoveAppeal(message);
        Optional<Command<CommandsListener>> controller = Optional.empty();
        Pattern compile = Pattern.compile(FIRST_WORD_FROM_STRING);
        Matcher matcher = compile.matcher(msg);
        if (matcher.find()) {
            String controllerName = matcher.group();
            controller = Optional.ofNullable(mappings.get(controllerName));
            controller.ifPresent(c -> userActionMap.put(username, controllerName));
        }
        return controller;
    }

    @NotNull
    private String checkAndRemoveAppeal(String message) {
        if (message.startsWith("!кенобот")) {
            message = removeAppeal(message);
        }
        return message;
    }

    public boolean isCached(String user) {
        return userActionMap.containsKey(user);
    }

    private String removeAppeal(String msg) {
        return msg.replaceAll(APPEAL_REGEX, "").trim();
    }

    private String removeAppealAndAction(String msg) {
        return msg.replaceAll(APPEAL_REGEX + ACTION_REGEX, "");
    }
}
