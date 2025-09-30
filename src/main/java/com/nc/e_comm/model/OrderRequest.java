package com.nc.e_comm.model;

import java.util.Map;

public class OrderRequest {
    private Map<Long, Integer> productQuantities;

    private Double totalAmount;

    public OrderRequest() {
    }

    public OrderRequest(Map<Long, Integer> productQuantities, Double totalAmount) {
        this.productQuantities = productQuantities;
        this.totalAmount = totalAmount;
    }

    public Map<Long, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<Long, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
