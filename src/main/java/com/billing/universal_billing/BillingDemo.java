
package com.billing.universal_billing;

import java.util.Scanner;

public class BillingDemo {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ProductManager productManager = new ProductManager();

        // Step 1: Owner enters products
        System.out.print("Enter number of products: ");
        int numberOfProducts = Integer.parseInt(sc.nextLine());

        for (int i = 1; i <= numberOfProducts; i++) {

            System.out.println("\nEnter details for product " + i);

            System.out.print("Product ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Product name: ");
            String name = sc.nextLine();

            System.out.print("Product price: ");
            double price = Double.parseDouble(sc.nextLine());

            System.out.print("Track stock? (yes/no): ");
            boolean trackStock =
                sc.nextLine().trim().equalsIgnoreCase("yes");

            int quantity = 0;

            if (trackStock) {
                System.out.print("Available stock: ");
                quantity = Integer.parseInt(sc.nextLine());
            }

            System.out.print("Barcode (press Enter to skip): ");
            String barcode = sc.nextLine();

            Product product = new Product(
                id, name, price, quantity, trackStock, barcode
            );

            productManager.addProduct(product);
            System.out.println("Product added successfully!");
        }

        // Step 2: Create billing service
        BillingService billingService =
            new BillingService(productManager);

        // Step 3: Create bill
        System.out.print("\nEnter bill ID: ");
        int billId = Integer.parseInt(sc.nextLine());

        System.out.print("Enter tax rate (%): ");
        double taxRate = Double.parseDouble(sc.nextLine());

        billingService.createBill(billId, taxRate);

        // Step 4: Select products for the bill
        System.out.print("How many different products to bill? ");
        int numberOfItems = Integer.parseInt(sc.nextLine());

        for (int i = 1; i <= numberOfItems; i++) {

            System.out.println("\nItem " + i);

            System.out.print("Enter product ID: ");
            int productId = Integer.parseInt(sc.nextLine());

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());

            boolean added = billingService.addProductToBill(
                billId, productId, quantity
            );

            if (added) {
                System.out.println("Item added to bill.");
            } else {
                System.out.println(
                    "Could not add item. Check product ID, quantity, "
                    + "stock, and whether it is already in the bill."
                );
            }
        }

        // Step 5: Apply a 10% discount
        billingService.applyDiscount(billId, 10);

        // Step 6: Finalize the bill and update stock
        boolean finalized = billingService.finalizeBill(billId);

        if (finalized) {
            System.out.println("Bill finalized successfully.");
        } else {
            System.out.println("Bill could not be finalized.");
        }

        // Step 7: Display the final bill
        System.out.println("\n======= FINAL BILL =======");
        billingService.displayBill(billId);

        sc.close();
    }
}
