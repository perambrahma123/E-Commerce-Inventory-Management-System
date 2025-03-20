 public static void main(String[] args) {
        loadInventory();
        while (true) {
            System.out.println("E-Commerce Inventory Management");
            System.out.println("1. Add Product");
            System.out.println("2. Update Stock");
            System.out.println("3. Update Price");
            System.out.println("4. Remove Product");
            System.out.println("5. View Products");
            System.out.println("6. Search Product");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1: addProduct(); break;
                case 2: updateStock(); break;
                case 3: updatePrice(); break;
                case 4: removeProduct(); break;
                case 5: viewProducts(); break;
                case 6: searchProduct(); break;
                case 7: saveInventory(); System.out.println("Exiting...\n"); return;
                default: System.out.println("Invalid Option!\n");
            }
        }
    }
