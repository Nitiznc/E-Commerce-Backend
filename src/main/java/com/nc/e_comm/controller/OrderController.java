package com.nc.e_comm.controller;

import com.nc.e_comm.dto.OrderDTO;
import com.nc.e_comm.model.OrderRequest;
import com.nc.e_comm.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place/{userId}")
    public OrderDTO placeOrder(@PathVariable Long userId, @RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(userId, orderRequest.getProductQuantities(), orderRequest.getTotalAmount());
    }

    @GetMapping("/all-orders")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrderByUser(@PathVariable Long userId) {
        return orderService.getOrderByUser(userId);
    }
}
