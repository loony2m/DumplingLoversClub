package com.example.dlc.services;

import com.example.dlc.models.Manufacturer;
import com.example.dlc.repositories.ManufacturerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    public Manufacturer addManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Transactional
    public void deleteManufacturer(Long manufacturerId) {
    Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new EntityNotFoundException("Производитель не найден с id: " + manufacturerId));

    // Удаление всех товаров, связанных с производителем
    manufacturer.getProducts().forEach(product -> product.setManufacturer(null));
    manufacturer.getProducts().clear();

    if (manufacturer.getBrands() != null && !manufacturer.getBrands().isEmpty()) {
        manufacturer.getBrands().forEach(brand -> brandRepository.delete(brand));
    } else {
        manufacturerRepository.delete(manufacturer);
    }
}
}
