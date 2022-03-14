package com.kylebyaka.kenobot.mvc.models.rest;

import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class FilmRestDTO {
    private static final String VIEW_FORMAT = """
            ```
            Название на русском: %s
            Название на английском: %s
            Год: %s
            Описание: %s
            Продолжительность: %s
            Страны: %s
            Жанры: %s
            Рейтинг: %f
            ```
            """;
    private long filmId;
    private String nameRu;
    private String nameEn;
    private String type;
    private String year;
    private String description;
    private String filmLength;
    private List<Map<String, String>> countries;
    private List<Map<String, String>> genres;
    private double rating;
    private int ratingVoteCount;
    private String posterUrl;
    private String posterUrlPreview;
    private boolean viewed;
    private List<Viewer> votes;
    private Viewer addedBy;


    public String getFormattedView() {
        return String.format(VIEW_FORMAT,
                nameRu,
                nameEn,
                year,
                description,
                filmLength,
                getFormattedMap(countries),
                getFormattedMap(genres),
                rating);
    }

    public EmbedBuilder getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE)
                .setThumbnail(posterUrlPreview)
                .addField("Название на русском", this.nameRu, true)
                .addField("Название на английском", this.nameEn, true)
                .addField("Оценка", String.valueOf(this.rating), false)
                .addField("Продолжительность", this.filmLength, true)
                .addField("Год", this.year, true)
                .addField("Описание", this.description, false)
                .addField("Страны", this.getFormattedMap(countries), true)
                .addField("Жанры", this.getFormattedMap(genres), true)
                .addField("Ссылка на кинопоиск", "https://www.kinopoisk.ru/film/" + filmId, false);
        return embedBuilder;
    }

    @NotNull
    private String getFormattedMap(List<Map<String, String>> maps) {
        return maps.stream()
                .flatMap(stringStringMap -> stringStringMap.entrySet().stream())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(", "));
    }
}
