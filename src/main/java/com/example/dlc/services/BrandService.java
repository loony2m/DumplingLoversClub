package com.example.dlc.services;

import com.example.dlc.models.Brand;
import com.example.dlc.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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
    public void deleteBrand(Long brandId) {
    Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new EntityNotFoundException("Марка не найдена с id: " + brandId));

    // Удаление всех товаров, связанных с производителем
    brand.getProducts().forEach(product -> product.setBrand(null));
    brand.getProducts().clear();

    // Проверка существования производителя
    if (brand.getManufacturer() != null) {
        Manufacturer manufacturer = brand.getManufacturer();
        // Удаление производителя
        manufacturerRepository.delete(manufacturer);
    } else {
        // Бренд не связан с производителем, можно удалить его напрямую
        brandRepository.delete(brand);
    }
}

}
