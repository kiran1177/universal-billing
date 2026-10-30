
package com.billing.universal_billing;

import java.util.HashMap;
import java.util.Map;

public class BillingService {

    private final ProductManager productManager;
    private final Map<Integer, Bill> bills = new HashMap<>();

    public BillingService(ProductManager productManager) {
        if (productManager == null) {
            throw new IllegalArgumentException(
                "ProductManager cannot be null"
            );
        }

        this.productManager = productManager;
    }

    public Bill createBill(int billId, double taxRate) {
        if (bills.containsKey(billId)) {
            throw new IllegalArgumentException(
                "Bill ID already exists"
            );
        }

        Bill bill = new Bill(billId, taxRate);
        bills.put(billId, bill);

        return bill;
    }

    public boolean addProductToBill(
            int billId, int productId, int quantity) {

        Bill bill = bills.get(billId);
        Product product = productManager.findById(productId);

        if (bill == null || product == null || quantity <= 0) {
            return false;
        }

        if (bill.isPaid()) {
            return false;
        }

        for (BillingItem item : bill.getItems()) {
            if (item.getProduct().getProductId() == productId) {
                return false;
            }
        }

        if (product.isTrackStock()
                && quantity > product.getQuantity()) {
            return false;
        }

        bill.addItem(
            new BillingItem(product, quantity, product.getPrice())
        );

        return true;
    }

    public boolean applyDiscount(int billId, double discountPercent) {
        Bill bill = bills.get(billId);

        if (bill == null || bill.isPaid()) {
            return false;
        }

        return bill.setDiscountPercent(discountPercent);
    }

    public boolean finalizeBill(int billId) {
        Bill bill = bills.get(billId);

        if (bill == null || bill.isPaid() || !bill.hasItems()) {
            return false;
        }

        // First validate stock for every item.
        for (BillingItem item : bill.getItems()) {
            Product product = item.getProduct();

            if (product.isTrackStock()
                    && item.getQuantity() > product.getQuantity()) {
                return false;
            }
        }

        // Only after all items pass validation, reduce stock.
        for (BillingItem item : bill.getItems()) {
            Product product = item.getProduct();

            if (product.isTrackStock()) {
                product.reduceStock(item.getQuantity());
            }
        }

        return bill.markAsPaid();
    }

    public Bill findBill(int billId) {
        return bills.get(billId);
    }

    public void displayBill(int billId) {
        Bill bill = bills.get(billId);

        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }

        bill.displayBill();
    }
}
