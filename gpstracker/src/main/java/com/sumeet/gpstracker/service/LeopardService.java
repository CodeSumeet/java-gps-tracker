package com.sumeet.gpstracker.service;

import com.sumeet.gpstracker.model.Leopard;
import com.sumeet.gpstracker.repository.LeopardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class LeopardService {

    private Random random = new Random();
    private static final double OUTER_RADIUS = 3000; // in meters
    private static final double INNER_RADIUS = 1000; // in meters
    private static final double PARK_CENTER_LAT = -2.3333;
    private static final double PARK_CENTER_LON = 34.8333;

    @Autowired
    private LeopardRepository leopardRepository;

    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(5000,10001)}")
    public void simulateLeopardMovements() {
        List<Leopard> leopards = leopardRepository.findAll();

        for (Leopard leopard : leopards) {
            double newLat = leopard.getLatitude() + (random.nextDouble() - 0.5) * 0.02;
            double newLon = leopard.getLongitude() + (random.nextDouble() - 0.5) * 0.02;

            double distanceToCenter = calculateDistance(PARK_CENTER_LAT, PARK_CENTER_LON, newLat, newLon);

            if (distanceToCenter > OUTER_RADIUS) {
                leopard.setShocked(true);
                leopard.setShockCount(leopard.getShockCount() + 1); // Increment shock count
                newLat = PARK_CENTER_LAT + (random.nextDouble() - 0.5) * 0.01;
                newLon = PARK_CENTER_LON + (random.nextDouble() - 0.5) * 0.01;
            } else if (distanceToCenter < INNER_RADIUS) {
                leopard.setShocked(true);
                leopard.setShockCount(leopard.getShockCount() + 1); // Increment shock count
                newLat = PARK_CENTER_LAT + (random.nextDouble() - 0.5) * 0.01 + 0.02;
                newLon = PARK_CENTER_LON + (random.nextDouble() - 0.5) * 0.01 + 0.02;
            } else {
                leopard.setShocked(false);
            }

            leopard.setLatitude(newLat);
            leopard.setLongitude(newLon);
            leopardRepository.save(leopard);

            sendLeopardUpdate(leopard); // Send updated data to the frontend
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // radius of Earth in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void sendLeopardUpdate(Leopard leopard) {
        // Send updated leopard data via WebSocket to frontend
    }
}
