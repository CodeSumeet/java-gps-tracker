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

    // Sanjay Gandhi National Park boundaries
    private static final double MIN_LATITUDE = 19.2147;
    private static final double MAX_LATITUDE = 19.3132;
    private static final double MIN_LONGITUDE = 72.8411;
    private static final double MAX_LONGITUDE = 72.9657;

    @Autowired
    public LeopardController(SimpMessagingTemplate messagingTemplate, LeopardRepository leopardRepository) {
        this.messagingTemplate = messagingTemplate;
        this.leopardRepository = leopardRepository;
    }

    @PostMapping
    public String addLeopard(@RequestBody Leopard leopard) {
        leopardRepository.save(leopard);
        return "Leopard added successfully!";
    }

    @GetMapping
    public List<Leopard> getLeopards() {
        return leopardRepository.findAll();
    }

    // Scheduled task to update leopard locations within park boundaries
    @Scheduled(fixedRate = 5000) // Update every 5 seconds
    public void updateLeopardLocations() {
        List<Leopard> leopards = leopardRepository.findAll();

        for (Leopard leopard : leopards) {
            // Small random movement within the park's latitude and longitude range
            double maxChange = 0.001; // Control the maximum change for smoother movement

            // Calculate new latitude and longitude
            double newLatitude = leopard.getLatitude() + (random.nextDouble() - 0.5) * maxChange;
            double newLongitude = leopard.getLongitude() + (random.nextDouble() - 0.5) * maxChange;

            // Restrict latitude and longitude within the park's boundaries
            newLatitude = Math.max(MIN_LATITUDE, Math.min(newLatitude, MAX_LATITUDE));
            newLongitude = Math.max(MIN_LONGITUDE, Math.min(newLongitude, MAX_LONGITUDE));

            // Set the new values
            leopard.setLatitude(newLatitude);
            leopard.setLongitude(newLongitude);

            // Save the updated leopard back to the database
            leopardRepository.save(leopard);

            // Send updated leopard location to clients via WebSocket
            messagingTemplate.convertAndSend("/topic/leopardLocation", leopard);
        }
    }
}
