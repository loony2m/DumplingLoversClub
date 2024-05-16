package com.example.dlc.services;

import com.example.dlc.models.*;
import com.example.dlc.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BrandRepository brandRepository;
    private final ManufacturerRepository manufacturerRepository;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getUsername());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getUsername(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void updateProduct(Long id, Long brandId, Long manufacturerId) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + brandId));
        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new IllegalArgumentException("Invalid manufacturer Id:" + manufacturerId));
        product.setBrand(brand);
        product.setManufacturer(manufacturer);
        productRepository.save(product);
    }

    public void addReviewToProduct(Long productId, Review review, String username/*, MultipartFile photo*/) {
        // Находим продукт по его ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Получаем пользователя по его имени
        User user = userRepository.findByUsername(username);

        // Получаем бренд продукта
        Brand brand = product.getBrand();

        // Получаем производителя продукта
        Manufacturer manufacturer = product.getManufacturer();

        // Проверяем, существует ли уже отзыв для данного продукта от данного пользователя
        Review existingReview = product.getReviews().stream()
                .filter(r -> r.getUser().equals(user))
                .findFirst()
                .orElse(null);

        // Если отзыв уже существует, обновляем его
        if (existingReview != null) {
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());
        } else {
            // Если отзыв не существует, добавляем новый отзыв
            Review newReview = new Review();
            newReview.setRating(review.getRating());
            newReview.setComment(review.getComment());
            newReview.setUser(user);
            newReview.setProduct(product);
            product.addReview(newReview);

        /*// Сохраняем фотографию вместе с отзывом
        if (photo != null && !photo.isEmpty()) {
            try {
                Image image = toImageEntity(photo);
                newReview.setPhoto(image);
            } catch (IOException e) {
                // Обработка ошибки при сохранении фотографии
                e.printStackTrace();
            }
        }*/
        }

        // Обновляем средний рейтинг бренда
        updateBrandAverageRating(brand);

        // Обновляем средний рейтинг производителя
        updateManufacturerAverageRating(manufacturer);

        // Сохраняем обновленный продукт
        productRepository.save(product);
    }


    private void updateManufacturerAverageRating(Manufacturer manufacturer) {
        List<Product> products = productRepository.findAllByManufacturer(manufacturer);
        double totalRating = 0;
        int numReviews = 0;
        for (Product product : products) {
            for (Review review : product.getReviews()) {
                totalRating += review.getRating();
                numReviews++;
            }
        }
        double averageRating = numReviews > 0 ? totalRating / numReviews : 0;
        manufacturer.setAverageRating(averageRating);
        manufacturerRepository.save(manufacturer);
    }

    private void updateBrandAverageRating(Brand brand) {
        List<Product> products = productRepository.findAllByBrand(brand);
        double totalRating = 0;
        int numReviews = 0;
        for (Product product : products) {
            for (Review review : product.getReviews()) {
                totalRating += review.getRating();
                numReviews++;
            }
        }
        double averageRating = numReviews > 0 ? totalRating / numReviews : 0;
        brand.setAverageRating(averageRating);
        brandRepository.save(brand);
    }


    public List<Review> getProductReviews(Long productId) {
        Product product = getProductById(productId);
        return product != null ? product.getReviews() : new ArrayList<>();
    }

    public void deleteReviewById(Long reviewId) {
        // Находим отзыв по его ID
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            // Удаляем отзыв из связанного продукта
            review.getProduct().getReviews().remove(review);
            // Удаляем отзыв из базы данных
            reviewRepository.deleteById(reviewId);
            log.info("Review with id = {} was deleted", reviewId);
        } else {
            log.error("Review with id = {} is not found", reviewId);
        }
    }

    public List<Product> listProductsSortedByBestBrands() {
        // Получение всех товаров из репозитория
        List<Product> allProducts = productRepository.findAll();

        // Сортировка товаров по лучшим брендам
        Collections.sort(allProducts, (p1, p2) -> {
            // Сравниваем средние рейтинги брендов товаров
            double rating1 = p1.getBrand().getAverageRating();
            double rating2 = p2.getBrand().getAverageRating();
            return Double.compare(rating2, rating1); // Сортировка по убыванию рейтинга
        });

        return allProducts;
    }

    public List<Product> listProductsSortedByWorstBrands() {
        // Получение всех товаров из репозитория
        List<Product> allProducts = productRepository.findAll();

        // Сортировка товаров по худшим брендам
        Collections.sort(allProducts, (p1, p2) -> {
            // Сравниваем средние рейтинги брендов товаров
            double rating1 = p1.getBrand().getAverageRating();
            double rating2 = p2.getBrand().getAverageRating();
            return Double.compare(rating1, rating2); // Сортировка по возрастанию рейтинга
        });

        return allProducts;
    }

    public List<Product> listProductsSortedByBestManufacturers() {
        // Получение всех товаров из репозитория
        List<Product> allProducts = productRepository.findAll();

        // Сортировка товаров по лучшим производителям
        Collections.sort(allProducts, (p1, p2) -> {
            // Сравниваем средние рейтинги производителей товаров
            double rating1 = p1.getManufacturer().getAverageRating();
            double rating2 = p2.getManufacturer().getAverageRating();
            return Double.compare(rating2, rating1); // Сортировка по убыванию рейтинга
        });

        return allProducts;
    }


    public List<Product> listProductsSortedByWorstManufacturers() {
        // Получение всех товаров из репозитория
        List<Product> allProducts = productRepository.findAll();

        // Сортировка товаров по худшим производителям
        Collections.sort(allProducts, (p1, p2) -> {
            // Сравниваем средние рейтинги производителей товаров
            double rating1 = p1.getManufacturer().getAverageRating();
            double rating2 = p2.getManufacturer().getAverageRating();
            return Double.compare(rating1, rating2); // Сортировка по возрастанию рейтинга
        });
        return allProducts;
    }
}
