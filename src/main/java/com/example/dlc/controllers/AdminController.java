package com.example.dlc.controllers;

import com.example.dlc.models.Brand;
import com.example.dlc.models.Manufacturer;
import com.example.dlc.models.Review;
import com.example.dlc.models.User;
import com.example.dlc.repositories.ReviewRepository;
import com.example.dlc.services.BrandService;
import com.example.dlc.services.ManufacturerService;
import com.example.dlc.services.ProductService;
import com.example.dlc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final BrandService brandService;
    @Autowired
    private final ManufacturerService manufacturerService;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;


    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.list());
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin";
    }

    @PostMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        // Находим отзывы, связанные с пользователем по его идентификатору
        List<Review> userReviews = reviewRepository.findByUserId(id);

        // Удаляем каждый отзыв
        for (Review review : userReviews) {
            productService.deleteReviewById(review.getId());
        }

        // Затем удаляем самого пользователя
        userService.deleteUser(id);

        return "redirect:/admin";
    }

    @GetMapping("/admin/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping("/admin/create")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getUsername() + " уже существует");
            return "user-create";
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/admin/edit/{id}")
    public String editUser(@PathVariable("id") Long id, @ModelAttribute("user") User updatedUser, Model model) {
        User existingUser = userService.getUserById(id);

        // Update the existing user with the new data
        existingUser.setUsername(updatedUser.getUsername());

        // Хешируем новый пароль перед обновлением
        String newPassword = updatedUser.getPassword();
        if (!newPassword.equals(existingUser.getPassword())) {
            // Проверяем, чтобы не хешировать пароль, если он не изменился
            existingUser.setPassword(passwordEncoder.encode(newPassword)); // Предполагается, что у вас есть passwordEncoder

            if (!userService.updateUser(id, existingUser)) {
                model.addAttribute("errorMessage", "Пользователь с email: " + updatedUser.getUsername() + " уже существует");
                return "user-edit";
            }
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/update-product/{id}")
    public String updateProduct(@PathVariable Long id, @RequestParam Long brand, @RequestParam Long manufacturer) {
        productService.updateProduct(id, brand, manufacturer);
        return "redirect:/product/{id}"; // Перенаправление на страницу информации о продукте после обновления
    }

    @GetMapping("/admin/create-manufacturer")
    public String showCreateManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "admin/manufacturer-create";
    }

    @PostMapping("/admin/create-manufacturer")
    public String createManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.addManufacturer(manufacturer);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete-manufacturer/{id}")
    public String deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturer(id);
        return "redirect:/admin"; // Перенаправление на страницу администратора после удаления
    }

    @GetMapping("/admin/create-brand")
    public String showCreateBrandForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "admin/brand-create";
    }

    @PostMapping("/admin/create-brand")
    public String createBrand(@ModelAttribute Brand brand) {
        brandService.addBrand(brand);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete-brand/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return "redirect:/admin"; // Перенаправление на страницу администратора после удаления
    }

    @GetMapping("/admin/manufacturers")
    public String getAllManufacturers(Model model) {
        List<Manufacturer> manufacturers = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", manufacturers);
        return "manufacturer-list"; // Возврат шаблона для отображения списка производителей
    }

    @GetMapping("/admin/brands")
    public String getAllBrands(Model model) {
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        return "brand-list"; // Возврат шаблона для отображения списка брендов
    }
}
