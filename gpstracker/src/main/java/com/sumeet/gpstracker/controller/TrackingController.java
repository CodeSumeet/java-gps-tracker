package com.sumeet.gpstracker.controller;

import com.sumeet.gpstracker.model.Leopard;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TrackingController {

    @MessageMapping("/updateLeopardLocation")
    @SendTo("/topic/leopardLocation")
    public Leopard updateLeopardLocation(Leopard leopard) {
        return leopard; // This will broadcast the updated location
    }
}
