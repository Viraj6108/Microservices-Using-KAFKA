package com.order.order.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OrderDTO {

    private int orderId ;
    private String item;
    private Integer quantity;
    private double price;
    public  enum STATUS{
        CONFIRMED, PENDING,CANCELLED
    }
    @Enumerated(EnumType.STRING)
    private Order.STATUS status;

    private String email;
    private String address;
    PaymentDTO paymentDTO;
    Shipment shipment;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order.STATUS getStatus() {
        return status;
    }

    public void setStatus(Order.STATUS status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    public void setPaymentDTO(PaymentDTO paymentDTO) {
        this.paymentDTO = paymentDTO;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
