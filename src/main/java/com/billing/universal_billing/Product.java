
package com.billing.universal_billing;

public class Product {

    private int productId;
    private String name;
    private double price;
    private int quantity;
    private boolean trackStock;
    private String barcode;

    // Parameterized constructor
    public Product(int productId, String name, double price,
                   int quantity, boolean trackStock, String barcode) {

        if (productId <= 0) {
            throw new IllegalArgumentException(
                "Product ID must be positive"
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Product name cannot be empty"
            );
        }

        if (price < 0 || !Double.isFinite(price)) {
            throw new IllegalArgumentException(
                "Price must be valid and non-negative"
            );
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(
                "Quantity cannot be negative"
            );
        }

        this.productId = productId;
        this.name = name.trim();
        this.price = price;
        this.quantity = quantity;
        this.trackStock = trackStock;

        if (barcode == null || barcode.trim().isEmpty()) {
            this.barcode = null;
        } else {
            this.barcode = barcode.trim();
        }
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isTrackStock() {
        return trackStock;
    }

    public String getBarcode() {
        return barcode;
    }

    // Update price
    public void setPrice(double price) {
        if (price < 0 || !Double.isFinite(price)) {
            throw new IllegalArgumentException(
                "Price must be valid and non-negative"
            );
        }

        this.price = price;
    }

    // Update quantity
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                "Quantity cannot be negative"
            );
        }

        this.quantity = quantity;
    }

    // Reduce stock after a successful sale
    public void reduceStock(int amount) {
        if (!trackStock) {
            return;
        }

        if (amount <= 0 || amount > quantity) {
            throw new IllegalArgumentException(
                "Invalid stock reduction"
            );
        }

        quantity = quantity - amount;
    }
}
