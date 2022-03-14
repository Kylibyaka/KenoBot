package com.kylebyaka.kenobot.mvc.services;

import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.mvc.repository.ViewerRepository;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ViewerService {

    @Autowired
    private ViewerRepository viewerRepository;

    public Optional<Viewer> findByName(String viewerName) {
        return viewerRepository.findByName(viewerName);
    }

    public Viewer add(Viewer viewer) {
        return viewerRepository.save(viewer);
    }

    public void addAll(Set<Viewer> viewers) {
        viewerRepository.saveAll(viewers);
    }

    public Optional<Viewer> findById(Long id) {
        return viewerRepository.findById(id);
    }

    public List<Viewer> findAll() {
        return viewerRepository.findAll();
    }

    public Viewer getOrCreateViewer(User user) {
        long viewerId = user.getIdLong();
        String name = user.getName();
        return findById(viewerId)
                .orElseGet(() -> add(Viewer.builder()
                        .viewerId(viewerId)
                        .name(name)
                        .build()));
    }

    public boolean existById(Long viewerId) {
        return viewerRepository.existsById(viewerId);
    }
}
