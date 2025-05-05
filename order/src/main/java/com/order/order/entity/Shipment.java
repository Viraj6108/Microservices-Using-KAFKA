package com.order.order.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class Shipment {
    private Integer shipmentId;
    private Integer orderId;
    public enum STATUS{
        SHIPPED, DELIVERED, INTRANSIT,CANCELLED
    }
    @Enumerated(EnumType.STRING)
    public STATUS status;

    public String deliveryDate;
    public Integer getShipmentId() {
        return shipmentId;
    }

    public OrderDTO setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
        return null;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
