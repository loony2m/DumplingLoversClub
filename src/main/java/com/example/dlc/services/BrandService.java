package com.example.dlc.services;

import com.example.dlc.models.Brand;
import com.example.dlc.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Transactional
    public void deleteBrand(Long manufacturerId) {
        Brand brand = brandRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found with id: " + manufacturerId));

        // Удаление всех товаров, связанных с производителем
        brand.getProducts().forEach(product -> product.setBrand(null));
        brand.getProducts().clear();

        // Удаление производителя
        brandRepository.delete(brand);
    }
}
