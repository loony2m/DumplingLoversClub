package com.example.dlc.services;

import com.example.dlc.models.Manufacturer;
import com.example.dlc.repositories.ManufacturerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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
        // Дополнительные проверки, например, на уникальность названия производителя
        return manufacturerRepository.save(manufacturer);
    }

    public void deleteManufacturer(Long id) {
        // Проверка существования производителя по ID перед удалением
        if (manufacturerRepository.existsById(id)) {
            manufacturerRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Manufacturer with id " + id + " not found");
        }
    }
}
