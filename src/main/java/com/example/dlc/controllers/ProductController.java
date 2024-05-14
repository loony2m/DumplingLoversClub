package com.example.dlc.controllers;

import com.example.dlc.models.*;
import com.example.dlc.repositories.UserRepository;
import com.example.dlc.services.BrandService;
import com.example.dlc.services.ManufacturerService;
import com.example.dlc.services.ProductService;
import com.example.dlc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final UserRepository userRepository;
    private final UserService userService;

    private final ManufacturerService manufacturerService;
    private final BrandService brandService;

    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";
    }

    @GetMapping("/products/top-brands-best")
    public String topBrandsBest(Model model) {
        List<Product> products = productService.listProductsSortedByBestBrands();
        model.addAttribute("products", products);

        // Добавляем атрибут текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username);
        model.addAttribute("user", currentUser);
        return "products";
    }

    @GetMapping("/products/top-brands-worst")
    public String topBrandsWorst(Model model) {
        List<Product> products = productService.listProductsSortedByWorstBrands();
        model.addAttribute("products", products);

        // Добавляем атрибут текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username);
        model.addAttribute("user", currentUser);
        return "products";
    }

    @GetMapping("/products/top-manufacturers-best")
    public String topManufacturersBest(Model model) {
        List<Product> products = productService.listProductsSortedByBestManufacturers();
        model.addAttribute("products", products);

        // Добавляем атрибут текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username);
        model.addAttribute("user", currentUser);
        return "products";
    }

    @GetMapping("/products/top-manufacturers-worst")
    public String topManufacturersWorst(Model model) {
        List<Product> products = productService.listProductsSortedByWorstManufacturers();
        model.addAttribute("products", products);

        // Добавляем атрибут текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username);
        model.addAttribute("user", currentUser);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());

        List<Brand> brands = brandService.getAllBrands();
        List<Manufacturer> manufacturers = manufacturerService.getAllManufacturers();
        model.addAttribute("brands", brands);
        model.addAttribute("manufacturers", manufacturers);

        model.addAttribute("id", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        model.addAttribute("currentUser", user);

        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product, Principal principal,
                                @AuthenticationPrincipal User user) throws IOException {
        product.setUser(user);
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
        return "redirect:/my/products";
    }

    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        List<Manufacturer> manufacturers = manufacturerService.getAllManufacturers();
        List<Brand> brands = brandService.getAllBrands();

        // Передаем их в модель
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("brands", brands);
        return "my-products";
    }

    @PostMapping("/product/{id}/review")
    public String addReview(@PathVariable Long id, Review review /*@RequestParam("photo") MultipartFile photo*/, Principal principal, Model model) {
        String username = principal.getName(); // Получаем имя текущего пользователя
        productService.addReviewToProduct(id, review, username/*, photo*/);

        // Получаем информацию о текущем пользователе из сервиса или репозитория
        User currentUser = userService.getUserByUsername(username);

        // Передаем информацию о текущем пользователе в модель
        model.addAttribute("currentUser", currentUser);

        return "redirect:/product/" + id;
    }

    @GetMapping("/product/{id}/reviews")
    public String getProductReviews(@PathVariable Long id, Model model) {
        List<Review> reviews = productService.getProductReviews(id);
        model.addAttribute("reviews", reviews);
        return "product-reviews";
    }

    @PostMapping("/product/{productId}/review/delete/{reviewId}")
    public String deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        productService.deleteReviewById(reviewId);
        return "redirect:/product/" + productId;
    }
}