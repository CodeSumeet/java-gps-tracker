package com.sumeet.gpstracker.config;

import com.sumeet.gpstracker.model.Human;
import com.sumeet.gpstracker.repository.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HumanDataInitializer implements CommandLineRunner {

    @Autowired
    private HumanRepository humanRepository;

    @Override
    public void run(String... args) throws Exception {
        if (humanRepository.count() == 0) { // Only add if DB is empty
            humanRepository.save(createHuman("Human1", 19.2500, 72.8600, 10.0));
            humanRepository.save(createHuman("Human2", 19.2700, 72.8800, 10.0));
            humanRepository.save(createHuman("Human3", 19.2900, 72.9000, 10.0));
            humanRepository.save(createHuman("Human4", 19.2600, 72.8900, 10.0));
            humanRepository.save(createHuman("Human5", 19.2800, 72.9100, 10.0));
            humanRepository.save(createHuman("Human6", 19.3000, 72.9200, 10.0));
            humanRepository.save(createHuman("Human7", 19.3100, 72.9300, 10.0));
            humanRepository.save(createHuman("Human8", 19.2400, 72.8600, 10.0));
            humanRepository.save(createHuman("Human9", 19.2200, 72.8450, 10.0));
            humanRepository.save(createHuman("Human10", 19.2700, 72.9500, 10.0));

            System.out.println("10 Humans added to the database.");
        }
    }

    // Method to create a new Human instance
    private Human createHuman(String name, double latitude, double longitude, double meatAvailable) {
        Human human = new Human();
        human.setName(name);
        human.setLatitude(latitude);
        human.setLongitude(longitude);
        human.setMeatAvailable(meatAvailable); // Initial meat available

        return human;
    }
}
