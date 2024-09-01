package com.app.photographers.service;

import com.app.photographers.model.Photographer;
import org.springframework.data.domain.Slice;

public interface PhotographerService {

    Photographer savePhotographer(Photographer photographer);

    Slice<Photographer> getAllPhotographers(int page);

    Photographer getPhotographerById(Long id);

    Slice<Photographer> getAllPhotographersByEventType(Long eventTypeId, int page);

    void deletePhotographer(Long id);

    Photographer updatePhotographer(Long id, Photographer photographer);
}
