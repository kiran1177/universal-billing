
package com.billing.universal_billing;

import java.util.ArrayList;
import java.util.List;

public class Bill {

    private final int billId;
    private final List<BillingItem> items;
    private final double taxRate;

    private double discountPercent;
    private String paymentStatus;

    public Bill(int billId, double taxRate) {
        if (billId <= 0) {
            throw new IllegalArgumentException(
                "Bill ID must be positive"
            );
        }

        if (taxRate < 0 || !Double.isFinite(taxRate)) {
            throw new IllegalArgumentException(
                "Tax rate must be valid and non-negative"
            );
        }

        this.billId = billId;
        this.taxRate = taxRate;
        this.items = new ArrayList<>();
        this.discountPercent = 0;
        this.paymentStatus = "UNPAID";
    }

    public void addItem(BillingItem item) {
        if (item == null) {
            throw new IllegalArgumentException(
                "Billing item cannot be null"
            );
        }

        if (!paymentStatus.equals("UNPAID")) {
            throw new IllegalStateException(
                "Cannot change a finalized bill"
            );
        }

        items.add(item);
    }

    public boolean setDiscountPercent(double discountPercent) {
        if (discountPercent < 0
                || discountPercent > 100
                || !Double.isFinite(discountPercent)
                || !paymentStatus.equals("UNPAID")) {
            return false;
        }

        this.discountPercent = discountPercent;
        return true;
    }

    public double calculateSubtotal() {
        double subtotal = 0;

        for (BillingItem item : items) {
            subtotal += item.calculateSubtotal();
        }

        return subtotal;
    }

    public double calculateDiscount() {
        return calculateSubtotal() * discountPercent / 100;
    }

    public double calculateTax() {
        double taxableAmount =
            calculateSubtotal() - calculateDiscount();

        return taxableAmount * taxRate / 100;
    }

    public double calculateTotal() {
        return calculateSubtotal()
            - calculateDiscount()
            + calculateTax();
    }

    public boolean markAsPaid() {
        if (!paymentStatus.equals("UNPAID") || items.isEmpty()) {
            return false;
        }

        paymentStatus = "PAID";
        return true;
    }

    public boolean isPaid() {
        return paymentStatus.equals("PAID");
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void displayBill() {
        System.out.println("Bill ID: " + billId);
        System.out.println("-------------------------");

        for (BillingItem item : items) {
            System.out.println(
                item.getProduct().getName()
                + " | Qty: " + item.getQuantity()
                + " | Unit price: " + item.getUnitPrice()
                + " | Subtotal: " + item.calculateSubtotal()
            );
        }

        System.out.println("-------------------------");
        System.out.printf("Subtotal: ₹%.2f%n", calculateSubtotal());
        System.out.printf(
            "Discount (%.2f%%): ₹%.2f%n",
            discountPercent, calculateDiscount()
        );
        System.out.printf("Tax: ₹%.2f%n", calculateTax());
        System.out.printf("Total: ₹%.2f%n", calculateTotal());
        System.out.println("Payment Status: " + paymentStatus);
    }

    public int getBillId() {
        return billId;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public List<BillingItem> getItems() {
        return List.copyOf(items);
    }
}
