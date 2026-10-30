
package com.billing.universal_billing;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (findById(product.getProductId()) != null) {
            throw new IllegalArgumentException("Product ID already exists");
        }

        String barcode = product.getBarcode();
        if (barcode != null && findByBarcode(barcode) != null) {
            throw new IllegalArgumentException("Barcode already exists");
        }

        products.add(product);
    }

    public Product findById(int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public Product findByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return null;
        }

        for (Product product : products) {
            if (barcode.equals(product.getBarcode())) {
                return product;
            }
        }
        return null;
    }

    public void displayAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {
            System.out.println(
                "ID: " + product.getProductId()
                + ", Name: " + product.getName()
                + ", Price: " + product.getPrice()
                + ", Quantity: "
                + (product.isTrackStock()
                    ? product.getQuantity() : "Not tracked")
                + ", Barcode: "
                + (product.getBarcode() == null
                    ? "Not assigned" : product.getBarcode())
            );
        }
    }

    public boolean updateProduct(int productId, double price, int quantity) {
        Product product = findById(productId);

        if (product == null || price < 0
                || !Double.isFinite(price) || quantity < 0) {
            return false;
        }

        product.setPrice(price);
        product.setQuantity(quantity);
        return true;
    }

    public boolean removeProduct(int productId) {
        Product product = findById(productId);

        if (product == null) {
            return false;
        }

        products.remove(product);
        return true;
    }

    public List<Product> getAllProducts() {
        return List.copyOf(products);
    }
}
