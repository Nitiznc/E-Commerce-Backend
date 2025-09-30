package com.nc.e_comm.dto;

import java.util.Date;
import java.util.List;

public class OrderDTO {

    private Long id;
    private double totalAmount;
    private String status;
    private Date orderDate;
    private String userName;
    private String userEmail;

    List<OrderItemDTO> orderItems;

    public OrderDTO() {
    }

    public OrderDTO(Long id, double totalAmount, String status, Date orderDate, String userName, String userEmail, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.userName = userName;
        this.userEmail = userEmail;
        this.orderItems = orderItems;
    }

    public OrderDTO(Long id, double totalAmount, String status, Date orderDate,List<OrderItemDTO> orderItems) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
