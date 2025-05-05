package com.order.order.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class PaymentDTO {

    private Integer paymentId;

    public  enum STATUS{
        SUCCESS, FAILED, PENDING,REFUND
    }
    @Enumerated(EnumType.STRING)
    private STATUS status;
    public enum PaymentMode {
        COD, ONLINE
    }
    @Enumerated(EnumType.STRING) // Store as a string in the database
    private PaymentMode paymentMode;

    private Integer orderId;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
