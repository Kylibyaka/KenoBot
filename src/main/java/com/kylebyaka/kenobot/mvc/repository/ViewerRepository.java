package com.kylebyaka.kenobot.mvc.repository;

import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewerRepository extends JpaRepository<Viewer, Long> {
    Optional<Viewer> findByName(String viewerName);
}
