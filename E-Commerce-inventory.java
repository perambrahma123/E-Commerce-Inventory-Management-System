import java.io.*;
import java.util.*;

class Product {
    String id;
    String name;
    double price;
    int quantity;

    Product(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-20s $%-10.2f %-10d", id, name, price, quantity);
    }
}

public class InventoryManager {
    private final Map<String, Product> inventory = new HashMap<>();
    private final String fileName = "inventory.csv";
    private final Scanner scanner = new Scanner(System.in);

    public void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Warning: File not found. Starting with an empty inventory.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    inventory.put(id, new Product(id, name, price, quantity));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Product p : inventory.values()) {
                bw.write(String.format("%s,%s,%f,%d%n", p.id, p.name, p.price, p.quantity));
            }
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    public void addProduct() {
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();

        if (inventory.containsKey(id)) {
            System.out.println("Product with this ID already exists.");
            return;
        }

        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline

        inventory.put(id, new Product(id, name, price, quantity));
        System.out.println("Product added successfully.");
    }

    public void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        String id = scanner.nextLine();

        Product p = inventory.get(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter new name [" + p.name + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) p.name = name;

        System.out.print("Enter new price [" + p.price + "]: ");
        String priceStr = scanner.nextLine();
        if (!priceStr.isEmpty()) p.price = Double.parseDouble(priceStr);

        System.out.print("Enter new quantity [" + p.quantity + "]: ");
        String qtyStr = scanner.nextLine();
        if (!qtyStr.isEmpty()) p.quantity = Integer.parseInt(qtyStr);

        System.out.println("Product updated successfully.");
    }

    public void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        String id = scanner.nextLine();

        if (inventory.remove(id) != null) {
            System.out.println("Product deleted.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void searchProduct() {
        System.out.print("Enter Product ID to search: ");
        String id = scanner.nextLine();

        Product p = inventory.get(id);
        if (p != null) {
            System.out.println("Product Found:\n" + p);
        } else {
            System.out.println("Product not found.");
        }
    }

    public void listProducts() {
        System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "Name", "Price", "Quantity");
        System.out.println("--------------------------------------------------------------");
        for (Product p : inventory.values()) {
            System.out.println(p);
        }
    }

    public void displayMenu() {
        System.out.println("\n--- Inventory Management ---");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. Search Product");
        System.out.println("5. List All Products");
        System.out.println("6. Save & Exit");
        System.out.print("Choose an option: ");
    }

    public void run() {
        loadFromFile();
        int choice;
        do {
            displayMenu();
            while (!scanner.hasNextInt()) {
                scanner.next(); // discard non-integer input
                System.out.print("Enter a valid option: ");
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> updateProduct();
                case 3 -> deleteProduct();
                case 4 -> searchProduct();
                case 5 -> listProducts();
                case 6 -> {
                    saveToFile();
                    System.out.println("Data saved. Exiting...");
                }
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 6);
    }

    public static void main(String[] args) {
        new InventoryManager().run();
    }
}
