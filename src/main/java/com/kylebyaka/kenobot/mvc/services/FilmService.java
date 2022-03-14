package com.kylebyaka.kenobot.mvc.services;

import com.kylebyaka.kenobot.mvc.exceptions.db.FilmNotFoundException;
import com.kylebyaka.kenobot.mvc.exceptions.db.ViewerNotFoundException;
import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.models.rest.ApiResponse;
import com.kylebyaka.kenobot.mvc.repository.FilmRepository;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.kylebyaka.kenobot.mvc.exceptions.db.FilmNotFoundException.FILM_NOT_FOUND_IN_DATABASE;
import static com.kylebyaka.kenobot.mvc.exceptions.db.ViewerNotFoundException.VIEWER_NOT_FOUND_IN_DATABASE;

@Service
public class FilmService {
    private static final String API_URL = "https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword";

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ViewerService viewerService;

    @Value("${com.kylebyaka.kenobot.x-api-key}")
    private String apiKey;

    @Transactional
    public void addByNameAndViewer(long id, String name, Viewer viewer) {
        if (!viewerService.existById(viewer.getViewerId())) {
            viewerService.add(viewer);
        }
        Film film = buildFilm(id, name, viewer);
        filmRepository.save(film);
    }

    public void addAll(List<Film> films) {
        filmRepository.saveAll(films);
    }

    @Transactional
    public void setViewed(String filmName) {
        Optional<Film> filmOptional = filmRepository.findByNameIgnoreCase(filmName);
        filmOptional.ifPresent(film -> film.setViewed(true));
    }

    public List<Film> findAllByFuzzyName(String name) {
        return filmRepository.findAllByNameContainingIgnoreCase(name);
    }

    public Optional<Film> findByName(String name) {
        return filmRepository.findByNameIgnoreCase(name);
    }

    public Optional<Film> findByFuzzyName(String name) {
        return filmRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Film> findAllByViewerVotesNotViewed(Viewer viewer) {
        return filmRepository.findAllByVotesContainsAndViewedIsFalse(viewer);
    }

    public List<Film> findAllNotViewed() {
        return filmRepository.findAllByViewedFalse();
    }

    public List<Film> findAllNotViewedPageable(Pageable pageable) {
        return filmRepository.findAllByViewedFalse(pageable);
    }

    public List<Film> findViewerAddedNotViewed(Viewer viewer) {
        return filmRepository.findAllByAddedByAndViewedFalse(viewer);
    }

    public void delete(Film film) {
        filmRepository.delete(film);
    }

    public void deleteAll(Collection<Film> film) {
        filmRepository.deleteAll(film);
    }

    @Transactional
    public void deleteByName(String name) {
        filmRepository.deleteByNameIgnoreCase(name);
        filmRepository.flush();
    }

    @Transactional
    public boolean addVote(String filmName, User user) {
        Viewer viewer = viewerService.getOrCreateViewer(user);

        Optional<Film> filmOptional = filmRepository.findByNameIgnoreCase(filmName);
        Film film = filmOptional.orElseThrow(() -> getFilmNotFoundException(filmName));
        film.getVotes().add(viewer);
        return true;
    }

    @Transactional
    public boolean removeVote(String filmName, User user) {
        Viewer viewer = viewerService.getOrCreateViewer(user);

        Optional<Film> filmOptional = filmRepository.findByNameContainingIgnoreCase(filmName);
        Film film = filmOptional.orElseThrow(() -> getFilmNotFoundException(filmName));
        film.getVotes().remove(viewer);
        return true;
    }

    public boolean existByName(String name) {
        return filmRepository.existsByNameIgnoreCase(name);
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    private FilmNotFoundException getFilmNotFoundException(String filmName) {
        return new FilmNotFoundException(String.format(FILM_NOT_FOUND_IN_DATABASE, filmName));
    }

    private ViewerNotFoundException getViewerNotFoundException(String userId) {
        return new ViewerNotFoundException(String.format(VIEWER_NOT_FOUND_IN_DATABASE, userId));
    }

    private Film buildFilm(long id, String name, Viewer viewer) {
        return Film.builder()
                .filmId(id)
                .name(StringUtils.capitalize(name))
                .votes(Collections.emptyList())
                .viewed(false)
                .addedBy(viewer)
                .build();
    }

    public List<String> findTopFiveFilms() {
        return filmRepository.findTopFive();
    }

    public void update(long id, String filmName) {
        Film film = filmRepository.findById(id).orElseThrow(getFilmNotFoundException());
        film.setName(filmName);
        filmRepository.save(film);
    }

    public Film findById(long id) {
        return filmRepository.findById(id).orElseThrow(getFilmNotFoundException());
    }

    private Supplier<RuntimeException> getFilmNotFoundException() {
        return () -> {
            throw new FilmNotFoundException("Film not found in the database");
        };
    }

    @NotNull
    public ResponseEntity<ApiResponse> makeFindRequest(String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-API-KEY", apiKey);

        HttpEntity<String> http = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(API_URL + "?keyword=" + message, HttpMethod.GET, http, ApiResponse.class);
    }
}
