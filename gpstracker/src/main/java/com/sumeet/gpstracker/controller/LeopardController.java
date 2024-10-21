package com.sumeet.gpstracker.controller;

import com.sumeet.gpstracker.model.Leopard;
import com.sumeet.gpstracker.repository.LeopardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/leopards")
@EnableScheduling
public class LeopardController {

    private final SimpMessagingTemplate messagingTemplate;
    private final LeopardRepository leopardRepository;

    // Random generator for location changes
    private final Random random = new Random();

    @Autowired
    public LeopardController(SimpMessagingTemplate messagingTemplate, LeopardRepository leopardRepository) {
        this.messagingTemplate = messagingTemplate;
        this.leopardRepository = leopardRepository;
    }

    // Endpoint to add leopards
    @PostMapping
    public String addLeopard(@RequestBody Leopard leopard) {
        leopardRepository.save(leopard);
        return "Leopard added successfully!";
    }

    // Endpoint to get all leopards' locations
    @GetMapping
    public List<Leopard> getLeopards() {
        return leopardRepository.findAll();
    }

    // Scheduled task to update leopard locations
    @Scheduled(fixedRate = 5000) // Update every 5 seconds
    public void updateLeopardLocations() {
        List<Leopard> leopards = leopardRepository.findAll();

        for (Leopard leopard : leopards) {
            // Update latitude and longitude with random values
            leopard.setLatitude(leopard.getLatitude() + (random.nextDouble() - 0.5) * 0.01);
            leopard.setLongitude(leopard.getLongitude() + (random.nextDouble() - 0.5) * 0.01);

            // Save the updated leopard back to the database
            leopardRepository.save(leopard);

            // Send updated leopard location to clients via WebSocket
            messagingTemplate.convertAndSend("/topic/leopardLocation", leopard);
        }
    }
}
