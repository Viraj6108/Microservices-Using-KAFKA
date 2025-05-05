package com.order.order.serviceImpl;

import com.google.gson.Gson;
import com.order.order.entity.Order;
import com.order.order.entity.PaymentDTO;
import com.order.order.entity.Shipment;

public class CombinedOrderDetails {

    private Order order;
    private PaymentDTO paymentDTO;
    private Shipment shipment;

    private Gson gson = new Gson();

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public void setOrderDetails(Order order) {
        this.order = order;
    }

    public void setPaymentDetails(String paymentJson) {
        this.paymentDTO = gson.fromJson(paymentJson, PaymentDTO.class);
    }
    public void setPaymentDetails(PaymentDTO paymentDTO) {
        this.paymentDTO = paymentDTO;
    }

    public void setShipmentDetails(String shipmentJson) {
        this.shipment = gson.fromJson(shipmentJson, Shipment.class);
    }
}
