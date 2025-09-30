package com.nc.e_comm.service;

import com.nc.e_comm.dto.OrderDTO;
import com.nc.e_comm.dto.OrderItemDTO;
import com.nc.e_comm.model.OrderItem;
import com.nc.e_comm.model.Orders;
import com.nc.e_comm.model.Product;
import com.nc.e_comm.model.User;
import com.nc.e_comm.repository.OrderRepository;
import com.nc.e_comm.repository.ProductRepository;
import com.nc.e_comm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    public OrderService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public OrderDTO placeOrder(Long userId, Map<Long, Integer> productQuantities, double totalAmount) {
        // Implementation here
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("Pending");
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(entry.getValue());
            orderItems.add(orderItem);

            orderItemDTOs.add(new OrderItemDTO(
                    product.getName(),
                    product.getPrice(),
                    entry.getValue()
            ));
        }

        order.setOrderItems(orderItems);
        Orders saveOrder = orderRepository.save(order);
        return new OrderDTO(
                saveOrder.getId(),
                saveOrder.getTotalAmount(),
                saveOrder.getStatus(),
                saveOrder.getOrderDate(),
                orderItemDTOs
        );
    }


    public List<OrderDTO> getAllOrders() {
        List<Orders> orders = orderRepository.findAllOrdersWithUsers();
        return orders.stream().map(this::convertToDTO).toList();
    }

    public OrderDTO convertToDTO(Orders orders) {
        List<OrderItemDTO> orderItemDTOS = orders.getOrderItems()
                .stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                )).toList();

        return new OrderDTO(
                orders.getId(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getOrderDate(),
                orders.getUser() != null ? orders.getUser().getName() : "Unknown",
                orders.getUser() != null ? orders.getUser().getEmail() : "Unknown",
                orderItemDTOS
        );
    }

    public List<OrderDTO> getOrderByUser(Long userId) {
        Optional<User> userOp = userRepository.findById(userId);
        if (userOp.isEmpty()) {
            throw new RuntimeException("User not found");
        } else {
            User user = userOp.get();
            List<Orders> orders = orderRepository.findByUser(user);
            return orders.stream().map(this::convertToDTO).toList();
        }
    }
}
