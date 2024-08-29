package com.app.photographers.repository;

import com.app.photographers.model.EventType;
import com.app.photographers.model.Photographer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"eventType"})
    Optional<Photographer> findById(@NonNull Long id);

    Slice<Photographer> findAllBy(Pageable pageable);

    Slice<Photographer> findAllByEventType(EventType eventType, Pageable pageable);
}
