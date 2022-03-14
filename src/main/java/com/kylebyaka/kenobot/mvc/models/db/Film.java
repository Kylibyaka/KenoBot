package com.kylebyaka.kenobot.mvc.models.db;

import lombok.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Table
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {
    public static final String URL_PATTERN = "https://discord.com/channels/271481332364738571/928769140829863976/";
    private static final String VIEW_FORMAT = """
            ```
            Название: %s
            Добавил: %s
            Просмотрен: %s
            Проголосовали: %s
            Ссылка: %s
            ```
            """;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    @Column(unique = true)
    private String name;
    private boolean viewed;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "Films_Viewers",
            joinColumns = {@JoinColumn(name = "film_id")},
            inverseJoinColumns = {@JoinColumn(name = "viewer_id")})
    private List<Viewer> votes;
    @ManyToOne
    @JoinColumn
    private Viewer addedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return filmId != null && Objects.equals(filmId, film.filmId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getFormattedView() {
        return String.format(VIEW_FORMAT,
                name,
                addedBy.getName(),
                getIsViewedView(),
                getVotesView(),
                URL_PATTERN + filmId);


    }

    public MessageEmbed getEmber() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(this.getName())
                .addField("Добавил", this.getAddedBy().getName(), true)
                .addField("Просмотрен", getIsViewedView(), true)
                .addField("Проголосовали", getVotesView(), false)
                .setColor(Color.ORANGE)
                .addField("Ссылка на сообщение", URL_PATTERN + this.getFilmId(), true);
        return embedBuilder.build();
    }

    public String getIsViewedView() {
        return this.isViewed() ? "Да" : "Нет";
    }

    @NotNull
    public String getVotesView() {
        String votes;
        if (this.getVotes().isEmpty()) {
            votes = "-";
        } else {
            votes = this.getVotes().stream()
                    .map(Viewer::getName)
                    .collect(Collectors.joining("\n"));
        }
        return votes;
    }

    public MessageEmbed buildEmbed(EmbedBuilder embed) {
        return embed
                .addField("Проголосовали", this.getVotesView(), true)
                .addField("Добавил", this.getAddedBy().getName(), true)
                .addField("Просмотрен", this.getIsViewedView(), true)
                .addField("Ссылка на сообщение", this.getJumpUrl(), false)
                .build();
    }

    public String getJumpUrl() {
        return URL_PATTERN + this.getFilmId();
    }
}
