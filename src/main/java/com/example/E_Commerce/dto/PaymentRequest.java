package com.example.E_Commerce.dto;

import java.util.List;

public class PaymentRequest {
    private List<Long> productIds;
    private List<Integer> quantities;
    private List<Long> prices;

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }

    public List<Integer> getQuantities() { return quantities; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }

    public List<Long> getPrices() { return prices; }
    public void setPrices(List<Long> prices) { this.prices = prices; }
}
