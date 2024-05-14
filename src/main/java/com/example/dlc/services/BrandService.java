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

    public void deleteBrand(Long id) {
        // Проверка существования бренда по ID перед удалением
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Brand with id " + id + " not found");
        }
    }
}
