package com.sumeet.gpstracker.controller;

import com.sumeet.gpstracker.model.Leopard;
import com.sumeet.gpstracker.repository.LeopardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leopards")
public class LeopardController {

    private final LeopardRepository leopardRepository;

    @Autowired
    public LeopardController(LeopardRepository leopardRepository) {
        this.leopardRepository = leopardRepository;
    }

    // Endpoint to add leopards
    @PostMapping
    public String addLeopard(@RequestBody Leopard leopard) {
        System.out.println("addLeopard called");
        System.out.println(leopard.getCallerId());
        leopardRepository.save(leopard); // Save leopard to the database
        return "Leopard added successfully!";
    }

    // Endpoint to get all leopards' locations
    @GetMapping
    public List<Leopard> getLeopards() {
        System.out.println("getLeopards called");
        System.out.println(leopardRepository.findAll());
        return leopardRepository.findAll(); // Fetch all leopards from the database
    }
}
