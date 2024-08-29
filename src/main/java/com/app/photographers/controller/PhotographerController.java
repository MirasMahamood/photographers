package com.app.photographers.controller;

import com.app.photographers.model.Photographer;
import com.app.photographers.service.PhotographerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/photographers")
@AllArgsConstructor
public class PhotographerController {

    private final PhotographerService photographerService;

    @PostMapping
    public Photographer createPhotographer(@Valid @RequestBody Photographer photographer) {
        return photographerService.savePhotographer(photographer);
    }

    @GetMapping
    public Map<String, Object> getAllPhotographers(@RequestParam(defaultValue = "0") int page) {
        Slice<Photographer> photographers = photographerService.getAllPhotographers(page);
        return generateGetAllResponse(photographers);
    }

    @GetMapping("/{id}")
    public Photographer getPhotographerById(@PathVariable Long id) {
        return photographerService.getPhotographerById(id);
    }

    @GetMapping("/event/{id}")
    public Map<String, Object> getAllPhotographersByEventType(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        Slice<Photographer> photographers = photographerService.getAllPhotographersByEventType(id, page);
        return generateGetAllResponse(photographers);
    }

    private Map<String, Object> generateGetAllResponse(Slice<Photographer> photographers) {
        Map<String, Object> response = new HashMap<>();
        response.put("photographers", photographers.getContent());
        response.put("currentPage", photographers.getNumber());
        response.put("totalItems", photographers.getNumberOfElements());
        response.put("hasNext", photographers.hasNext());
        return response;
    }
}
