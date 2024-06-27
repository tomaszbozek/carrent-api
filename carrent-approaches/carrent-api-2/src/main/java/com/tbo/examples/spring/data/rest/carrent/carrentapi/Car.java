package com.tbo.examples.spring.data.rest.carrent.carrentapi;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String model;
    @Column(name = "registration_number")
    private String registrationNumber;
    @Column(name = "production_year")
    private Integer productionYear;
}
