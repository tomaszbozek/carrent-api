package com.tbo.examples.spring.data.rest.carrent.carrent_api_3;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CarrentApi3ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String CAR_KEY = "car";

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.mockMvc(mockMvc);
	}

	@Test
	public void getCarById() throws Exception {
		RestAssuredMockMvc
				.given()
				.when()
				.get("/cars/1")
				.then()
				.statusCode(200)
				.body("id", equalTo(1))
				.body("type", equalTo("SUV"))
				.body("model", equalTo("x5"))
				.body("registrationNumber", equalTo("1243"))
				.body("productionYear", equalTo(2024));
	}

	@Test
	public void getAllCars() throws Exception {
		RestAssuredMockMvc
				.given()
				.when()
				.get("/cars")
				.then()
				.statusCode(200)
				.body("$.size()", greaterThan(0))
				.body("id", hasItems(1, 2))
				.body("type", hasItems("SUV", "Sedan"));
	}

	@Test
	public void createCar() throws Exception {
		RestAssuredMockMvc
				.given()
				.contentType("application/json")
				.body("{\"id\": 3, \"type\": \"Truck\", \"model\": \"F150\", \"registrationNumber\": \"9999\", \"productionYear\": 2023}")
				.when()
				.post("/cars")
				.then()
				.statusCode(200)
				.body("id", equalTo(3))
				.body("type", equalTo("Truck"))
				.body("model", equalTo("F150"))
				.body("registrationNumber", equalTo("9999"))
				.body("productionYear", equalTo(2023));

		// Verify the car is stored in Redis
		CarDto car = objectMapper.readValue(redisTemplate.opsForValue().get(CAR_KEY), CarDto.class);
		assert car != null;
		assert car.id() == 3;
		assert car.type().equals("Truck");
		assert car.model().equals("F150");
		assert car.registrationNumber().equals("9999");
		assert car.productionYear() == 2023;
	}
}
