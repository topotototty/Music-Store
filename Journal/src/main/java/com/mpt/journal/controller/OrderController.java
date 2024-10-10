package com.mpt.journal.controller;

import com.mpt.journal.model.AlbumModel;
import com.mpt.journal.model.OrderModel;
import com.mpt.journal.model.UserModel;
import com.mpt.journal.model.OrderStatus;
import com.mpt.journal.repository.AlbumRepository;
import com.mpt.journal.repository.UserRepository;
import com.mpt.journal.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // Отображение страницы создания нового заказа
    @GetMapping("/create")
    public String showCreateOrderPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("albums", albumRepository.findAll());
        return "orderCreate";  // Возвращаем шаблон для создания заказа
    }

    // Создание нового заказа
    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("orderModel") OrderModel orderModel, BindingResult bindingResult,
                              @RequestParam Long userId, @RequestParam List<Long> albumIds, Model model) {

        Optional<UserModel> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Пользователь с таким идентификатором не найден");
            return "orderCreate";
        }
        UserModel user = userOptional.get();

        OrderModel order = new OrderModel();
        order.setUser(user);
        order.setStatus(OrderStatus.NEW);
        order.setOrderNumber(generateOrderNumber());
        order.setTotalPrice(BigDecimal.ZERO);

        List<AlbumModel> albums = albumRepository.findAllById(albumIds);
        if (albums.isEmpty()) {
            model.addAttribute("error", "Ни один из переданных альбомов не найден");
            return "orderCreate";
        }
        order.setAlbums(albums);

        BigDecimal totalPrice = albums.stream()
                .map(AlbumModel::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        orderService.saveOrder(order);

        return "redirect:/orders";
    }

    // Отображение всех заказов
    @GetMapping
    public String getAllOrders(Model model) {
        List<OrderModel> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderControl"; // Возвращаем шаблон списка заказов
    }

    // Удаление заказа
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{orderId}")
    public String showEditOrderPage(@PathVariable Long orderId, Model model) {
        OrderModel order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());

        return "orderEdit";
    }

    @PostMapping("/edit/{orderId}")
    public String updateOrder(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        OrderModel existingOrder = orderService.getOrderById(orderId);

        if (existingOrder == null) {
            return "redirect:/orders";
        }

        existingOrder.setStatus(status);
        orderService.saveOrder(existingOrder);

        return "redirect:/orders";
    }

    @GetMapping("/orders/my")
    public String viewUserOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        UserModel user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь с логином " + login + " не найден"));
        List<OrderModel> userOrders = orderService.getOrdersByUser(user);

        model.addAttribute("orders", userOrders);

        return "index";
    }

    private String generateOrderNumber() {
        return String.valueOf(System.currentTimeMillis()).substring(8);
    }
}
