package com.example.dlc.models;

import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Brand {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    private double averageRating;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAverageRating() {
        return averageRating;
    }
}


