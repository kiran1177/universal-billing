package com.billing.universal_billing;

public class BillingItem {

    private Product product;
    private int quantity;
    private double unitPrice;

    public BillingItem(Product product, int quantity, double unitPrice) {
        if (product == null) {
             throw new IllegalArgumentException("Product cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.product=product;
        this.quantity=quantity;
        this.unitPrice=product.getPrice();
    }
    public double calculateSubtotal() {
        return unitPrice * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}