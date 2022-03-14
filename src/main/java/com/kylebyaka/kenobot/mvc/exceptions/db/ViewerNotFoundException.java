package com.kylebyaka.kenobot.mvc.exceptions.db;

public class ViewerNotFoundException extends DatabaseException {
    public static final String VIEWER_NOT_FOUND_IN_DATABASE = "Viewer %s not found in database";

    public ViewerNotFoundException(String message) {
        super(message);
    }
}
