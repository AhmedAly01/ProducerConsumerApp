package com.main.Backend.model;

public class Product {
    private Integer prodNumber;
    private Long delayTime;

    public Product(Integer prodNumber, Long delayTime) {
        this.prodNumber = prodNumber;
        this.delayTime = delayTime;
    }

    public Integer getProdNumber() {
        return prodNumber;
    }

    public void setProdNumber(Integer prodNumber) {
        this.prodNumber = prodNumber;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }
}
