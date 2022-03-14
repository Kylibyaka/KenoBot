package com.kylebyaka.kenobot.mvc.models.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Table
@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Viewer {
    @Id
    private Long viewerId;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Films_Viewers",
            inverseJoinColumns = {@JoinColumn(name = "film_id")},
            joinColumns = {@JoinColumn(name = "viewer_id")})
    private List<Film> votes;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "filmId")
    private List<Film> films;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Viewer viewer = (Viewer) o;
        return viewerId != null && Objects.equals(viewerId, viewer.viewerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

