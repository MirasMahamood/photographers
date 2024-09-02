package com.app.photographers.unit.service;

import com.app.photographers.exception.ErrorMessages;
import com.app.photographers.exception.ResourceNotFoundException;
import com.app.photographers.model.EventType;
import com.app.photographers.model.Photographer;
import com.app.photographers.repository.PhotographerRepository;
import com.app.photographers.service.EventTypeService;
import com.app.photographers.service.impl.PhotographerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PhotographerServiceTest {

    @Mock
    private EventTypeService eventTypeService;

    @Mock
    private PhotographerRepository photographerRepository;

    @InjectMocks
    private PhotographerServiceImpl photographerService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(photographerService, "pageSize", 20);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void savePhotographer_Success() {
        Photographer photographer = new Photographer();
        photographer.setId(1L);
        EventType eventType = new EventType();
        eventType.setId(1L);
        photographer.setEventType(eventType);

        when(photographerRepository.save(any(Photographer.class))).thenReturn(photographer);
        when(eventTypeService.getEventTypeById(anyLong())).thenReturn(eventType);

        Photographer savedPhotographer = photographerService.savePhotographer(photographer);

        assertNotNull(savedPhotographer);
        assertEquals(1L, savedPhotographer.getId());
        assertEquals(1L, savedPhotographer.getEventType().getId());
        verify(photographerRepository, times(1)).save(any(Photographer.class));
    }

    @Test
    void getPhotographerById_Success() {
        Photographer photographer = new Photographer();
        photographer.setId(1L);

        when(photographerRepository.findById(anyLong())).thenReturn(Optional.of(photographer));

        Photographer foundPhotographer = photographerService.getPhotographerById(1L);

        assertNotNull(foundPhotographer);
        assertEquals(1L, foundPhotographer.getId());
        verify(photographerRepository, times(1)).findById(anyLong());
    }

    @Test
    void getPhotographerById_NotFound() {
        when(photographerRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            photographerService.getPhotographerById(1L);
        });

        assertEquals(ErrorMessages.ERROR_PHOTOGRAPHER_NOT_FOUND, exception.getMessage());
        verify(photographerRepository, times(1)).findById(anyLong());
    }

    @Test
    void deletePhotographer_Success() {
        Photographer photographer = new Photographer();
        photographer.setId(1L);

        when(photographerRepository.findById(anyLong())).thenReturn(Optional.of(photographer));
        doNothing().when(photographerRepository).delete(any(Photographer.class));

        photographerService.deletePhotographer(1L);

        verify(photographerRepository, times(1)).findById(anyLong());
        verify(photographerRepository, times(1)).delete(any(Photographer.class));
    }

    @Test
    void updatePhotographer_Success() {
        Photographer existingPhotographer = new Photographer();
        existingPhotographer.setId(1L);
        Photographer updatedPhotographer = new Photographer();
        updatedPhotographer.setName("Updated Name");

        when(photographerRepository.findById(anyLong())).thenReturn(Optional.of(existingPhotographer));
        when(photographerRepository.save(any(Photographer.class))).thenReturn(updatedPhotographer);

        Photographer result = photographerService.updatePhotographer(1L, updatedPhotographer);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(photographerRepository, times(1)).findById(anyLong());
        verify(photographerRepository, times(1)).save(any(Photographer.class));
    }

    @Test
    void getAllPhotographers_Success() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("modifiedDate").descending());
        Slice<Photographer> photographers = getPhotographers();

        when(photographerRepository.findAllBy(pageable)).thenReturn(photographers);

        Slice<Photographer> result = photographerService.getAllPhotographers(0);

        assertNotNull(result);
        assertEquals(2, result.getNumberOfElements());
        assertFalse(result.hasNext());
        verify(photographerRepository, times(1)).findAllBy(any(PageRequest.class));
    }

    @Test
    void getAllPhotographersByEventType_Success() {
        EventType eventType = new EventType();
        eventType.setId(1L);
        Slice<Photographer> photographers = getPhotographers();

        when(eventTypeService.getEventTypeById(anyLong())).thenReturn(eventType);
        when(photographerRepository.findAllByEventType(any(EventType.class), any(PageRequest.class))).thenReturn(photographers);

        Slice<Photographer> result = photographerService.getAllPhotographersByEventType(1L, 0);

        assertNotNull(result);
        assertEquals(2, result.getNumberOfElements());
        verify(photographerRepository, times(1)).findAllByEventType(any(EventType.class), any(PageRequest.class));
    }

    private Slice<Photographer> getPhotographers() {
        EventType eventType = new EventType();
        eventType.setId(1L);

        List<Photographer> photographerList = new ArrayList<>();
        Photographer photographer1 = new Photographer();
        photographer1.setId(1L);
        photographer1.setName("Photographer 1");
        photographer1.setEventType(eventType);
        photographerList.add(photographer1);

        Photographer photographer2 = new Photographer();
        photographer2.setId(2L);
        photographer2.setName("Photographer 2");
        photographer2.setEventType(eventType);
        photographerList.add(photographer2);

        Pageable pageable = PageRequest.of(0, 20, Sort.by("modifiedDate").descending());
        return new PageImpl<>(photographerList, pageable, photographerList.size());
    }
}