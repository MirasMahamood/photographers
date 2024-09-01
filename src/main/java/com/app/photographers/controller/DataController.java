package com.app.photographers.controller;

import com.app.photographers.model.EventType;
import com.app.photographers.model.Photographer;
import com.app.photographers.repository.PhotographerRepository;
import com.app.photographers.service.EventTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// FIXME: Only for loading data in db for testing purposes. Remove in production.
@RestController
@RequestMapping("api/data")
@AllArgsConstructor
public class DataController {

    private final EventTypeService eventTypeService;

    private final PhotographerRepository photographerRepository;

    @PostMapping("/generateData")
    public void generateData(@RequestParam int count, @RequestParam Long eventTypeId) {
        String description = "In the depth of the forest, where sunlight filtered through the canopy in shimmering patches, a solitary fox prowled stealthily, its russet fur blending seamlessly with the dappled shadows. In its keen eyes gleamed the wisdom of ages, inherited from ancestors who had roamed these woods since time immemorial. Each step was deliberate, calculated, as it sought sustenance amidst the verdant undergrowth. Meanwhile, high above, a lone hawk soared on thermals, its wings spread wide as it rode the currents of the sky with effortless grace. From its vantage point, it a surveyed the landscape below, keen eyesight spotting the slightest movement amidst the tapestry of green. With a sudden dive, it plummeted earthward, talons outstretched, aiming for its unsuspecting prey. The heart of the ancient forest, sunlight filtered through the dense canopy, casting a mosaic of light and shadow on the forest floor. Birds chirped melodiously, while a gentle breeze whispered secrets among the towering trees.";
        List<Photographer> photographers = new ArrayList<>();
        EventType eventType = eventTypeService.getEventTypeById(eventTypeId);
        for (int i = 0; i < count; i++) {
            Photographer photographer = new Photographer();
            photographer.setEventType(eventType);
            photographer.setName("Photographer");
            photographer.setContact("1234567890");
            photographer.setDescription(description);
            photographer.setAvatar("http://test/avatar.jpg");
            photographers.add(photographer);
        }
        photographerRepository.saveAll(photographers);
    }
}
