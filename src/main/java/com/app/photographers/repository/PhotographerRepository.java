package com.app.photographers.repository;

import com.app.photographers.model.EventType;
import com.app.photographers.model.Photographer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    Slice<Photographer> findAllBy(Pageable pageable);

    Slice<Photographer> findAllByEventType(EventType eventType, Pageable pageable);
}
