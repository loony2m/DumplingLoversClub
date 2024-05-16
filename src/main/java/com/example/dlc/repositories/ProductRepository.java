package com.example.dlc.repositories;

import com.example.dlc.models.Brand;
import com.example.dlc.models.Manufacturer;
import com.example.dlc.models.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"reviews"})
    Optional<Product> findById(Long id);

    List<Product> findByTitle(String title);

    List<Product> findAllByManufacturer(Manufacturer manufacturer);

    List<Product> findAllByBrand(Brand brand);
}
