package com.sumeet.gpstracker.controller;

import com.sumeet.gpstracker.model.Human;
import com.sumeet.gpstracker.model.Leopard;
import com.sumeet.gpstracker.repository.HumanRepository;
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
    private final Random random = new Random();
    private final HumanRepository humanRepository;

    // Serengeti National Park center
    private static final double CENTER_LATITUDE = -2.3333;
    private static final double CENTER_LONGITUDE = 34.8333;

    // Define radius in kilometers
    private static final double OUTER_RADIUS_KM = 3.0; // Outer circle radius
    private static final double INNER_RADIUS_KM = 1.0; // Inner circle radius

    @Autowired
    public LeopardController(SimpMessagingTemplate messagingTemplate, LeopardRepository leopardRepository, HumanRepository humanRepository) {
        this.messagingTemplate = messagingTemplate;
        this.leopardRepository = leopardRepository;
        this.humanRepository = humanRepository;
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
    @Scheduled(fixedRate = 1000) // Update every 1 second
    public void updateLeopardLocations() {
        List<Leopard> leopards = leopardRepository.findAll();

        for (Leopard leopard : leopards) {
            // Random movement
            double maxChange = 0.001; // Control the maximum change for smoother movement
            double newLatitude = leopard.getLatitude() + (random.nextDouble() - 0.5) * maxChange;
            double newLongitude = leopard.getLongitude() + (random.nextDouble() - 0.5) * maxChange;

            // Reset crossed flag
            leopard.setCrossedOuterFence(false); // Reset crossed status

            // Check if the leopard tries to escape
            if (isOutsideOuterCircle(newLatitude, newLongitude)) {
                int randomAction = random.nextInt(2); // Generate random 0 or 1

                if (randomAction == 1) {
                    // Leopard successfully escapes the outer fence
                    leopard.setCrossedOuterFence(true); // Mark as crossed

                    // Set leopard's position to a random location outside the outer circle
                    double[] escapePosition = getRandomOutsideOuterCircle();
                    leopard.setLatitude(escapePosition[0]);
                    leopard.setLongitude(escapePosition[1]);

                    messagingTemplate.convertAndSend("/topic/leopardCrossedFence", leopard); // Notify frontend
                } else {
                    // Leopard gets shocked, move back inside the outer circle
                    double[] correctedPosition = getPositionWithinOuterCircle(newLatitude, newLongitude);
                    newLatitude = correctedPosition[0];
                    newLongitude = correctedPosition[1];
                    leopard.setShocked(true);  // Mark leopard as shocked
                    leopard.setShockCount(leopard.getShockCount() + 1);  // Increment shock count
                }
            } else if (isInsideInnerCircle(newLatitude, newLongitude)) {
                // Leopard tries to enter inner circle, move back outside and mark as "shocked"
                double[] correctedPosition = getPositionOutsideInnerCircle(newLatitude, newLongitude);
                newLatitude = correctedPosition[0];
                newLongitude = correctedPosition[1];
                leopard.setShocked(true);  // Mark leopard as shocked
                leopard.setShockCount(leopard.getShockCount() + 1);  // Increment shock count
            } else {
                // Normal movement, no shock
                leopard.setShocked(false);
            }

            // Update leopard's location if it didn't escape
            if (!leopard.isCrossedOuterFence()) {
                leopard.setLatitude(newLatitude);
                leopard.setLongitude(newLongitude);
            }

            // Save updated leopard back to the database
            leopardRepository.save(leopard);

            // Send updated leopard location to clients via WebSocket
            messagingTemplate.convertAndSend("/topic/leopardLocation", leopard);
        }
    }

    // Check if the leopard is outside the outer circle
    private boolean isOutsideOuterCircle(double latitude, double longitude) {
        double distance = haversineDistance(CENTER_LATITUDE, CENTER_LONGITUDE, latitude, longitude);
        return distance > OUTER_RADIUS_KM;
    }

    // Check if the leopard is inside the inner circle
    private boolean isInsideInnerCircle(double latitude, double longitude) {
        double distance = haversineDistance(CENTER_LATITUDE, CENTER_LONGITUDE, latitude, longitude);
        return distance < INNER_RADIUS_KM;
    }

    // Get a position corrected inside the outer circle
    private double[] getPositionWithinOuterCircle(double latitude, double longitude) {
        // Adjust location to be exactly at the boundary of the outer circle
        double distance = haversineDistance(CENTER_LATITUDE, CENTER_LONGITUDE, latitude, longitude);
        double scale = OUTER_RADIUS_KM / distance;
        return new double[]{
                CENTER_LATITUDE + (latitude - CENTER_LATITUDE) * scale,
                CENTER_LONGITUDE + (longitude - CENTER_LONGITUDE) * scale
        };
    }

    // Get a position corrected outside the inner circle
    private double[] getPositionOutsideInnerCircle(double latitude, double longitude) {
        double distance = haversineDistance(CENTER_LATITUDE, CENTER_LONGITUDE, latitude, longitude);
        double scale = INNER_RADIUS_KM / distance;
        return new double[]{
                CENTER_LATITUDE + (latitude - CENTER_LATITUDE) * scale,
                CENTER_LONGITUDE + (longitude - CENTER_LONGITUDE) * scale
        };
    }

    // Generate a random position outside the outer circle
    private double[] getRandomOutsideOuterCircle() {
        // Random angle
        double angle = random.nextDouble() * 2 * Math.PI;
        // Distance greater than outer radius to ensure it's outside
        double distance = OUTER_RADIUS_KM + random.nextDouble() * 2; // Ensuring at least 2 km away

        double latitude = CENTER_LATITUDE + (distance / 111) * Math.sin(angle); // Approximate conversion km to degrees
        double longitude = CENTER_LONGITUDE + (distance / 111) * Math.cos(angle); // Approximate conversion km to degrees

        return new double[]{latitude, longitude};
    }

    // Haversine formula to calculate distance between two coordinates
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    @PostMapping("/{callerId}/feed")
    public String feedLeopard(@PathVariable String callerId, @RequestParam double amount) {
        Leopard leopard = leopardRepository.findById(callerId).orElse(null);
        if (leopard != null) {
            Human human = humanRepository.findById("someHumanId").orElse(null); // Fetch a human
            if (human != null && human.getMeatAvailable() >= amount) {
                leopard.addFood(amount); // Update the leopard's food
                human.setMeatAvailable(human.getMeatAvailable() - amount); // Decrease the human's meat
                leopardRepository.save(leopard);
                humanRepository.save(human);

                // Notify frontend about feeding
                messagingTemplate.convertAndSend("/topic/leopardFed", new FeedingEvent(leopard.getName(), amount));

                return "Food added successfully!";
            }
            return "Not enough meat available!";
        }
        return "Leopard not found!";
    }

    // Inner class to represent feeding events
    private static class FeedingEvent {
        private final String name;
        private final double amountEaten;

        public FeedingEvent(String name, double amountEaten) {
            this.name = name;
            this.amountEaten = amountEaten;
        }

        public String getName() {
            return name;
        }

        public double getAmountEaten() {
            return amountEaten;
        }
    }

}
