package com.example.dlc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class Brand {
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
    private List<Manufacturer> banufacturers;

    @Setter
    @Getter
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}


