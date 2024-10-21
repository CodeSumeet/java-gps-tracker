package com.sumeet.gpstracker;

import com.sumeet.gpstracker.model.Leopard;
import com.sumeet.gpstracker.repository.LeopardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GpstrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpstrackerApplication.class, args);
	}

}
