package com.tbo.examples.spring.data.rest.carrent.carrent_api_3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CarRepository repository) {
        return args -> {
            repository.save(new Car(1L, "SUV", "x5", "1243", 2024));
            repository.save(new Car(2L, "Sedan", "A6", "5678", 2022));
        };
    }
}