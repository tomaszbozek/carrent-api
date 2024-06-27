package com.carrent.carrent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CarRentApi {

    private final CarRepository carRepository;

    public CarRentApi(@Autowired CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @PostMapping("/cars")
    public CarDto addCar(CarDto carDto) {
        return Optional.of(carRepository.save(new Car(
                carDto.type(),
                carDto.model(),
                carDto.registrationNumber(),
                carDto.productionYear()
        ))).map(car -> new CarDto(
                car.getId(), car.getType(), car.getModel(), car.getRegistrationNumber(),
                car.getProductionYear()
        )).get();
    }

    @GetMapping("/cars")
    public List<CarDto> get() {
        List<CarDto> result = new ArrayList<>();

        for (Car car : carRepository.findAll()) {
            result.add(
                    new CarDto(
                            car.getId(), car.getType(), car.getModel(), car.getRegistrationNumber(),
                            car.getProductionYear()
                    )

            );
        }
        return result;
        
    }

    public record CarDto(Long id, String type, String model, String registrationNumber, Integer productionYear) {
    }
}