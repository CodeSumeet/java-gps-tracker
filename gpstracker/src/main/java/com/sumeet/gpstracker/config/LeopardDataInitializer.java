package com.sumeet.gpstracker.config;

import com.sumeet.gpstracker.model.Leopard;
import com.sumeet.gpstracker.repository.LeopardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LeopardDataInitializer implements CommandLineRunner {

    @Autowired
    private LeopardRepository leopardRepository;

    @Override
    public void run(String... args) throws Exception {
        if (leopardRepository.count() == 0) { // Only add if DB is empty
            leopardRepository.save(createLeopard("ABC123", "Leopard1", 3, 19.2500, 72.8600));
            leopardRepository.save(createLeopard("ABC124", "Leopard2", 4, 19.2700, 72.8800));
            leopardRepository.save(createLeopard("ABC125", "Leopard3", 5, 19.2900, 72.9000));
            leopardRepository.save(createLeopard("ABC126", "Leopard4", 6, 19.2600, 72.8900));
            leopardRepository.save(createLeopard("ABC127", "Leopard5", 7, 19.2800, 72.9100));
            leopardRepository.save(createLeopard("ABC128", "Leopard6", 8, 19.3000, 72.9200));
            leopardRepository.save(createLeopard("ABC129", "Leopard7", 9, 19.3100, 72.9300));
            leopardRepository.save(createLeopard("ABC130", "Leopard8", 10, 19.2400, 72.8600));
            leopardRepository.save(createLeopard("ABC131", "Leopard9", 11, 19.2200, 72.8450));
            leopardRepository.save(createLeopard("ABC132", "Leopard10", 12, 19.2700, 72.9500));

            System.out.println("10 Leopards added to the database.");
        }
    }

    // Method to create a new Leopard instance
    private Leopard createLeopard(String callerId, String name, int shockCount, double latitude, double longitude) {
        Leopard leopard = new Leopard();
        leopard.setCallerId(callerId);
        leopard.setName(name);
        leopard.setShockCount(shockCount);
        leopard.setLatitude(latitude);
        leopard.setLongitude(longitude);
        leopard.setShocked(false);  // Initially not shocked
        leopard.setCrossedOuterFence(false);  // Initially has not crossed the outer fence
        return leopard;
    }
}
