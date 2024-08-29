package com.app.photographers.service.impl;

import com.app.photographers.exception.ErrorMessages;
import com.app.photographers.exception.ResourceNotFoundException;
import com.app.photographers.model.EventType;
import com.app.photographers.model.Photographer;
import com.app.photographers.repository.PhotographerRepository;
import com.app.photographers.service.EventTypeService;
import com.app.photographers.service.PhotographerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotographerServiceImpl implements PhotographerService {

    private final EventTypeService eventTypeService;
    private final PhotographerRepository photographerRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @CachePut(value = "photographer", key = "#photographer.id")
    @Override
    public Photographer savePhotographer(Photographer photographer) {
        photographer = photographerRepository.save(photographer);
        photographer.setEventType(eventTypeService.getEventTypeById(photographer.getEventType().getId()));
        return photographer;
    }

    @Override
    public Slice<Photographer> getAllPhotographers(int page) {
        return photographerRepository.findAllBy(getPageable(page));
    }

    @Cacheable(value = "photographer", key = "#id")
    @Override
    public Photographer getPhotographerById(Long id) {
        Optional<Photographer> photographer = photographerRepository.findById(id);
        if (photographer.isEmpty()) {
            log.error(ErrorMessages.ERROR_PHOTOGRAPHER_NOT_FOUND + " id: {}", id);
            throw new ResourceNotFoundException(ErrorMessages.ERROR_PHOTOGRAPHER_NOT_FOUND);
        }
        return photographer.get();
    }

    @Override
    public Slice<Photographer> getAllPhotographersByEventType(Long eventTypeId, int page) {
        EventType eventType = eventTypeService.getEventTypeById(eventTypeId);
        return photographerRepository.findAllByEventType(eventType, getPageable(page));
    }

    @CacheEvict(value = "photographer", key = "#id")
    @Override
    public void deletePhotographer(Long id) {
        photographerRepository.delete(getPhotographerById(id));
    }

    @CachePut(value = "photographer", key = "#id")
    @Override
    public void updatePhotographer(Long id, Photographer photographer) {
        Photographer _photographer = getPhotographerById(id);
        _photographer.setName(photographer.getName());
        _photographer.setAvatar(photographer.getAvatar());
        _photographer.setContact(photographer.getContact());
        _photographer.setDescription(photographer.getDescription());
        _photographer.setEventType(photographer.getEventType());
        photographerRepository.save(_photographer);
    }

    private Pageable getPageable(int page) {
        return PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
    }
}
