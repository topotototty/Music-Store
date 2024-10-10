package com.mpt.journal.controller;

import com.mpt.journal.model.OrderModel;
import com.mpt.journal.model.UserModel;
import com.mpt.journal.service.OrderService;
import com.mpt.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    public IndexController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/orders/my")
    public String viewUserOrders(Model model) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();

        List<OrderModel> userOrders = orderService.getOrdersByUserId(currentUser.getUserId());

        model.addAttribute("orders", userOrders);

        return "userOrders"; // Возвращаем шаблон для отображения заказов пользователя
    }

}
