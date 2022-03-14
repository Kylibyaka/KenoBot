package com.kylebyaka.kenobot.mvc.controllers.api;

import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kylebyaka.kenobot.utils.BotUtil.FIRST_WORD_FROM_STRING;

/***
 * This class is needed for command with nested logic.
 * @param <T> in which class this command will be stored
 * @param <R> commands what type will be stored in this class. Most of the time it's the same class that extend this class
 */
public abstract class NestedCommand<T, R> implements Command<T> {
    private static final String EMPTY_STRING = "";

    /***
     * Simple actions cache
     */
    private final Map<String, String> userActionCache = new HashMap<>();

    private Map<String, Command<R>> nameCommandMap;

    @Autowired
    public void populateMappings(List<Command<R>> commands) {
        nameCommandMap = commands.stream().collect(Collectors.toMap(Command::getName, Function.identity()));
    }

    @Override
    public Response execute(String message, User user) {
        String clearedMessage;
        if (isCached(user.getName())) {
            clearedMessage = message;
        } else {
            clearedMessage = removeCommandFromMessage(message);
        }
        Command<R> command = getCommand(message, user);
        return getResponse(clearedMessage, user, command);
    }

    @Override
    public void clear(String username) {
        String cache = userActionCache.get(username);
        if (cache != null) {
            nameCommandMap.get(cache).clear(username);
            userActionCache.remove(username);
        }
    }

    @Override
    public String getType() {
        return Type.INTERMEDIATE.getName();
    }

    private Command<R> getCommand(String message, User user) {
        String name = user.getName();
        if (isCached(name)) return nameCommandMap.get(userActionCache.get(name));

        return cacheAndGetCommand(message, name);
    }

    private boolean isCached(String username) {
        return userActionCache.get(username) != null;
    }

    private Command<R> cacheAndGetCommand(String message, String username) {
        String commandName = getCommandNameWithDescription(message);
        Command<R> command = nameCommandMap.get(commandName);
//        if (!commandName.isBlank()) {
        if (command instanceof NestedCommand<?, ?>) {
            userActionCache.put(username, commandName);
        }
        return command;
    }

    private String getCommandNameWithDescription(String message) {
        Pattern compile = Pattern.compile(FIRST_WORD_FROM_STRING);
        Matcher matcher = compile.matcher(message);
        if (matcher.find()) {
            return matcher.group();
        }
        return EMPTY_STRING;
    }

    private String removeCommandFromMessage(String message) {
        return message.replaceAll(FIRST_WORD_FROM_STRING, EMPTY_STRING).trim();
    }

    private Response getResponse(String message, User user, Command<?> command) {
        if (command == null) {
            StringBuilder finalMessage = new StringBuilder("Выбери команду: ");
            String commands;
            if (message.equals("--подробнее")) {
                commands = getCommands(this::getCommandNameWithDescription);
            } else {
                commands = getCommands(this::getCommandName);
            }
            finalMessage.append("\n")
                    .append(commands)
                    .append("\nИли **сброс**, чтобы вернуться к началу. --подробнее, чтобы отобразить описание команд.");
            List<LayoutComponent> actionRows = getButtons();
            return Response.builder()
                    .message("Выбери команду:\n")
                    .components(actionRows)
                    .build();
        } else {
            return command.execute(message, user);
        }
    }

    @NotNull
    private List<LayoutComponent> getButtons() {
        List<LayoutComponent> actionRows = new ArrayList<>();
        List<Command<R>> collect = nameCommandMap.values().stream().toList();
        for (int i = 0; i < collect.size() - 1; i = i + 2) {
            Command<R> first = collect.get(i);
            Command<R> second = collect.get(i + 1);
            ActionRow itemComponents = ActionRow.of(buildButton(first.getType(), first.getName()),
                    buildButton(second.getType(), second.getName()));
            actionRows.add(itemComponents);
        }
        ActionRow of;
        if (collect.size() % 2 != 0) {
            Command<R> last = collect.get(collect.size() - 1);
            of = ActionRow.of(buildButton(last.getType(), last.getName()),
                    buildButton(Type.INTERMEDIATE.getName(), "сброс"));
        } else {
            of = ActionRow.of(buildButton(Type.INTERMEDIATE.getName(), "сброс"));
        }
        actionRows.add(of);
        return actionRows;
    }

    @NotNull
    private Button buildButton(String type, String label) {
        return Button.primary(type + ":" + label, label);
    }

    private String getCommands(Function<Command<R>, String> mapper) {
        return nameCommandMap.values().stream().map(mapper).collect(Collectors.joining("\n"));
    }

    private String getCommandName(Command<R> c) {
        return "**" + c.getName() + "**";
    }

    private String getCommandNameWithDescription(Command<R> c) {
        String result = "**" + c.getName() + "**";
        String description = c.getDescription();
        if (!description.isEmpty()) {
            result = result + ": " + description;
        }
        return result;
    }
}
