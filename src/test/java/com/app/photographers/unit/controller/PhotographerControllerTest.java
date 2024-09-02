package com.app.photographers.unit.controller;

import com.app.photographers.controller.PhotographerController;
import com.app.photographers.exception.ErrorMessages;
import com.app.photographers.exception.ResourceNotFoundException;
import com.app.photographers.model.Photographer;
import com.app.photographers.service.PhotographerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class PhotographerControllerTest {

    @Mock
    private PhotographerService photographerService;

    @InjectMocks
    private PhotographerController photographerController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void createPhotographer() {
        Photographer photographer = new Photographer();
        when(photographerService.savePhotographer(any(Photographer.class))).thenReturn(photographer);

        Photographer result = photographerController.createPhotographer(photographer);
        assertEquals(photographer, result);
    }

    @Test
    void createPhotographerWithNullName() {
        Photographer photographer = new Photographer();
        photographer.setName(null);
        doThrow(new IllegalArgumentException("Name is required")).when(photographerService).savePhotographer(photographer);
        assertThrows(IllegalArgumentException.class, () -> photographerController.createPhotographer(photographer));
    }

    @Test
    void createPhotographerWithLongDescription() {
        Photographer photographer = new Photographer();
        photographer.setDescription("a".repeat(1001));
        doThrow(new IllegalArgumentException("Description is too long")).when(photographerService).savePhotographer(photographer);
        assertThrows(IllegalArgumentException.class, () -> photographerController.createPhotographer(photographer));
    }

    @Test
    void getAllPhotographers() {
        Slice<Photographer> photographers = new SliceImpl<>(Collections.singletonList(new Photographer()));
        when(photographerService.getAllPhotographers(0)).thenReturn(photographers);

        Map<String, Object> result = photographerController.getAllPhotographers(0);
        assertEquals(1, result.get("totalItems"));
        assertEquals(0, result.get("currentPage"));
        assertEquals(false, result.get("hasNext"));
    }

    @Test
    void getAllPhotographersWithEmptyList() {
        Slice<Photographer> photographers = new SliceImpl<>(Collections.emptyList());
        when(photographerService.getAllPhotographers(0)).thenReturn(photographers);

        Map<String, Object> result = photographerController.getAllPhotographers(0);
        assertEquals(0, result.get("totalItems"));
        assertEquals(0, result.get("currentPage"));
        assertEquals(false, result.get("hasNext"));
    }

    @Test
    void getPhotographerById() {
        Photographer photographer = new Photographer();
        when(photographerService.getPhotographerById(anyLong())).thenReturn(photographer);

        Photographer result = photographerController.getPhotographerById(1L);
        assertEquals(photographer, result);
    }

    @Test
    void getPhotographerByIdNotFound() {
        when(photographerService.getPhotographerById(anyLong())).thenThrow(new ResourceNotFoundException(ErrorMessages.ERROR_PHOTOGRAPHER_NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> photographerController.getPhotographerById(1L));
    }

    @Test
    void getAllPhotographersByEventType() {
        Slice<Photographer> photographers = new SliceImpl<>(Collections.singletonList(new Photographer()));
        when(photographerService.getAllPhotographersByEventType(anyLong(), any(Integer.class))).thenReturn(photographers);

        Map<String, Object> result = photographerController.getAllPhotographersByEventType(1L, 0);
        assertEquals(1, result.get("totalItems"));
        assertEquals(0, result.get("currentPage"));
        assertEquals(false, result.get("hasNext"));
    }

    @Test
    void getAllPhotographersByEventTypeWithEmptyList() {
        Slice<Photographer> photographers = new SliceImpl<>(Collections.emptyList());
        when(photographerService.getAllPhotographersByEventType(anyLong(), any(Integer.class))).thenReturn(photographers);

        Map<String, Object> result = photographerController.getAllPhotographersByEventType(1L, 0);
        assertEquals(0, result.get("totalItems"));
        assertEquals(0, result.get("currentPage"));
        assertEquals(false, result.get("hasNext"));
    }

    @Test
    void createPhotographerServiceThrowsException() {
        Photographer photographer = new Photographer();
        doThrow(new RuntimeException("Service exception")).when(photographerService).savePhotographer(any(Photographer.class));

        assertThrows(RuntimeException.class, () -> photographerController.createPhotographer(photographer));
    }
}