package com.kylebyaka.kenobot.mvc.models.rest;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private String keyword;
    private int pageCount;
    private List<FilmRestDTO> films;
}
