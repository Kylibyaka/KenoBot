package com.kylebyaka.kenobot.mvc.repository;

import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    Optional<Film> findByNameContainingIgnoreCase(String name);

    List<Film> findAllByVotesContainsAndViewedIsFalse(Viewer viewer);

    List<Film> findAllByViewedFalse();

    List<Film> findAllByViewedFalse(Pageable pageable);
    
    List<Film> findAllByAddedByAndViewedFalse(Viewer viewer);

    void deleteByName(String message);

    void deleteByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Film> findAllByNameContainingIgnoreCase(String name);

    Optional<Film> findByNameIgnoreCase(String name);

    @Query(value = "SELECT f.name " +
            "from film as f " +
            "         join films_viewers as fv on f.film_id = fv.film_id " +
            "where f.viewed is false " +
            "group by f.name " +
            "order by count(fv.viewer_id) desc " +
            "limit 5", nativeQuery = true)
    List<String> findTopFive();
}
