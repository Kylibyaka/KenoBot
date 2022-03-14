package com.kylebyaka.kenobot.mvc.controllers.api;

import com.kylebyaka.kenobot.listeners.CommandsListener;
import com.kylebyaka.kenobot.mvc.models.rest.Response;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Collections;
import java.util.Set;

/***
 * This class accumulate all commands regarding one category.
 * All classes that implement this interface parametrized with {@link CommandsListener} is stored in it.
 * @param <T> class where this command will be stored
 */
public interface Command<T> {

    /***
     * @param message name of a controller to delegate processing
     * @param user user that trigger processing
     * @return Response with message and components
     */
    Response execute(String message, User user);

    /***
     * @return name of a command to invoke with
     */
    String getName();

    String getType();

    default String getDescription() {
        return "";
    }

    /***
     * Method for controllers that have nested logic need to override to clear all cache
     * @param username username for what cache should be cleared
     */
    default void clear(String username) {
    }

    static Command<CommandsListener> getDefaultCommand(final Set<String> mappings) {
        return new Command<>() {
            @Override
            public Response execute(String message, User user) {
                return Response.builder()
                        .message("Выбери категорию:\n")
                        .components(Collections.singletonList(ActionRow.of(mappings.stream()
                                .map(s -> Button.primary(Type.INTERMEDIATE.getName() + ":" + s, s))
                                .toList())))
                        .build();
            }

            @Override
            public String getName() {
                return "default";
            }

            @Override
            public String getType() {
                return Type.ACTION.getName();
            }
        };
    }

    enum Type {
        ACTION("action"), INPUT("input"), INTERMEDIATE("intermediate"), PAGINATION("pagination");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
