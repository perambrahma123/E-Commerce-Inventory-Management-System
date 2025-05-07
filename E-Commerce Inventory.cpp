#include <iostream>
#include <fstream>
#include <unordered_map>
#include <sstream>
#include <iomanip>

struct Product {
    std::string id;
    std::string name;
    double price;
    int quantity;
};

class InventoryManager {
private:
    std::unordered_map<std::string, Product> inventory;

public:
    void loadFromFile(const std::string& filename) {
        std::ifstream file(filename);
        std::string line;

        if (!file) {
            std::cerr << "Warning: Could not open file. Starting with empty inventory.\n";
            return;
        }

        while (getline(file, line)) {
            std::stringstream ss(line);
            Product p;
            std::string priceStr, quantityStr;

            getline(ss, p.id, ',');
            getline(ss, p.name, ',');
            getline(ss, priceStr, ',');
            getline(ss, quantityStr, ',');

            p.price = std::stod(priceStr);
            p.quantity = std::stoi(quantityStr);

            inventory[p.id] = p;
        }

        file.close();
    }

    void saveToFile(const std::string& filename) {
        std::ofstream file(filename);
        for (const auto& [id, product] : inventory) {
            file << product.id << "," << product.name << ","
                 << product.price << "," << product.quantity << "\n";
        }
        file.close();
    }

    void addProduct() {
        Product p;
        std::cout << "Enter Product ID: ";
        std::cin >> p.id;

        if (inventory.find(p.id) != inventory.end()) {
            std::cout << "Product with this ID already exists.\n";
            return;
        }

        std::cin.ignore();
        std::cout << "Enter Product Name: ";
        getline(std::cin, p.name);
        std::cout << "Enter Price: ";
        std::cin >> p.price;
        std::cout << "Enter Quantity: ";
        std::cin >> p.quantity;

        inventory[p.id] = p;
        std::cout << "Product added successfully.\n";
    }

    void updateProduct() {
        std::string id;
        std::cout << "Enter Product ID to update: ";
        std::cin >> id;

        auto it = inventory.find(id);
        if (it == inventory.end()) {
            std::cout << "Product not found.\n";
            return;
        }

        Product& p = it->second;
        std::cout << "Enter new name [" << p.name << "]: ";
        std::cin.ignore();
        getline(std::cin, p.name);
        std::cout << "Enter new price [" << p.price << "]: ";
        std::cin >> p.price;
        std::cout << "Enter new quantity [" << p.quantity << "]: ";
        std::cin >> p.quantity;

        std::cout << "Product updated successfully.\n";
    }

    void deleteProduct() {
        std::string id;
        std::cout << "Enter Product ID to delete: ";
        std::cin >> id;

        if (inventory.erase(id)) {
            std::cout << "Product deleted.\n";
        } else {
            std::cout << "Product not found.\n";
        }
    }

    void searchProduct() {
        std::string id;
        std::cout << "Enter Product ID to search: ";
        std::cin >> id;

        auto it = inventory.find(id);
        if (it != inventory.end()) {
            const Product& p = it->second;
            std::cout << "ID: " << p.id << "\n"
                      << "Name: " << p.name << "\n"
                      << "Price: $" << p.price << "\n"
                      << "Quantity: " << p.quantity << "\n";
        } else {
            std::cout << "Product not found.\n";
        }
    }

    void listProducts() {
        std::cout << std::left << std::setw(10) << "ID" 
                  << std::setw(20) << "Name"
                  << std::setw(10) << "Price"
                  << std::setw(10) << "Quantity" << "\n";

        std::cout << "-----------------------------------------------------\n";
        for (const auto& [id, p] : inventory) {
            std::cout << std::left << std::setw(10) << p.id
                      << std::setw(20) << p.name
                      << std::setw(10) << p.price
                      << std::setw(10) << p.quantity << "\n";
        }
    }
};

void displayMenu() {
    std::cout << "\n--- Inventory Management ---\n";
    std::cout << "1. Add Product\n";
    std::cout << "2. Update Product\n";
    std::cout << "3. Delete Product\n";
    std::cout << "4. Search Product\n";
    std::cout << "5. List All Products\n";
    std::cout << "6. Save & Exit\n";
    std::cout << "Choose an option: ";
}

int main() {
    InventoryManager manager;
    manager.loadFromFile("inventory.csv");

    int choice;
    do {
        displayMenu();
        std::cin >> choice;
        switch (choice) {
            case 1: manager.addProduct(); break;
            case 2: manager.updateProduct(); break;
            case 3: manager.deleteProduct(); break;
            case 4: manager.searchProduct(); break;
            case 5: manager.listProducts(); break;
            case 6:
                manager.saveToFile("inventory.csv");
                std::cout << "Data saved. Exiting...\n";
                break;
            default: std::cout << "Invalid option.\n"; break;
        }
    } while (choice != 6);

    return 0;
}
