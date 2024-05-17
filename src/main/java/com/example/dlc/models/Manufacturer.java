package com.example.dlc.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Transient;

import javax.persistence.*;
import java.util.List;

@Entity
public class Manufacturer {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private double averageRating;

    @Setter
    @Getter
    @Transient
    private List<Brand> brands;

    @Setter
    @Getter
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
