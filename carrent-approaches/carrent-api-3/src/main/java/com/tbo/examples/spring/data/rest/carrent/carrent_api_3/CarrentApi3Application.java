package com.tbo.examples.spring.data.rest.carrent.carrent_api_3;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class CarrentApi3Application {

	public static void main(String[] args) {
		SpringApplication.run(CarrentApi3Application.class, args);
	}
}

@RestController
@RequiredArgsConstructor
class CarrentApi3 {
	private final CarRepository carRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	@PostMapping("/cars")
	public CarDto addCar(@RequestBody CarDto carDto) {
		redisTemplate.opsForValue().set("car", asString(carDto));
		return Optional.of(carRepository.save(new Car(
				carDto.type(),
				carDto.model(),
				carDto.registrationNumber(),
				carDto.productionYear()
		)))
				.map(car -> new CarDto(
								car.getId(), car.getType(), car.getModel(), car.getRegistrationNumber(),
								car.getProductionYear())
				)
				.orElseThrow();
	}

	@SneakyThrows
	private String asString(CarDto carDto) {
		return objectMapper.writeValueAsString(carDto);
	}

	@GetMapping("/cars")
	public List<CarDto> get() {
		return StreamSupport.stream(carRepository.findAll().spliterator(), false)
				.map(car -> new CarDto(
						car.getId(), car.getType(), car.getModel(), car.getRegistrationNumber(),
						car.getProductionYear()
				))
				.toList();
	}

	@GetMapping("/cars/{id}")
	public CarDto getCarById(@PathVariable long id) {
		return carRepository.findById(id).map(car -> new CarDto(
				car.getId(), car.getType(), car.getModel(), car.getRegistrationNumber(),
				car.getProductionYear()
		)).orElseThrow();
	}
}

record CarDto(Long id, String type, String model, String registrationNumber, Integer productionYear) {
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String type;
	private String model;
	@Column(name = "registration_number")
	private String registrationNumber;
	@Column(name = "production_year")
	private Integer productionYear;

	public Car(String type, String model, String registrationNumber, Integer productionYear) {
		this.type = type;
		this.model = model;
		this.registrationNumber = registrationNumber;
		this.productionYear = productionYear;
	}
}

@Repository
interface CarRepository extends CrudRepository<Car, Long> {
}

