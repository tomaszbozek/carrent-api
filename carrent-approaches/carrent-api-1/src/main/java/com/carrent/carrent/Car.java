package com.carrent.carrent;

import jakarta.persistence.*;

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
    public Car() {
    }
    public Car(String type, String model, String registrationNumber, Integer productionYear) {
        this.type = type;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.productionYear = productionYear;
    }
    public Long getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getModel() {
        return model;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public Integer getProductionYear() {
        return productionYear;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }
}
