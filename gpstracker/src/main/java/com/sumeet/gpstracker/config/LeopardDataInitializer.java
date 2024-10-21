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
            leopardRepository.save(new Leopard("ABC123", "Leopard1", 3, 20.5937, 78.9629));
            leopardRepository.save(new Leopard("ABC124", "Leopard2", 4, 20.5938, 78.9630));
            leopardRepository.save(new Leopard("ABC125", "Leopard3", 5, 20.5939, 78.9631));
            leopardRepository.save(new Leopard("ABC126", "Leopard4", 6, 20.5940, 78.9632));
            leopardRepository.save(new Leopard("ABC127", "Leopard5", 7, 20.5941, 78.9633));
            leopardRepository.save(new Leopard("ABC128", "Leopard6", 8, 20.5942, 78.9634));
            leopardRepository.save(new Leopard("ABC129", "Leopard7", 9, 20.5943, 78.9635));
            leopardRepository.save(new Leopard("ABC130", "Leopard8", 10, 20.5944, 78.9636));
            leopardRepository.save(new Leopard("ABC131", "Leopard9", 11, 20.5945, 78.9637));
            leopardRepository.save(new Leopard("ABC132", "Leopard10", 12, 20.5946, 78.9638));

            System.out.println("10 Leopards added to the database.");
        }
    }
}
