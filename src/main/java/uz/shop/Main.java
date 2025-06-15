package uz.shop;

import uz.shop.model.*;
import uz.shop.role.Role;
import uz.shop.service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static Scanner scStr = new Scanner(System.in);
    public static UserService userService = new UserService();
    public static User currentUser = new User();
    public static CategoryService categoryService = new CategoryService();
    public static ProductService productService = new ProductService();
    public static BasketService basketService = new BasketService();
    public static BillService billService = new BillService();

    public static void main(String[] args) {

    }

    public void menu() {
        int option = 100;
        while (option != 0) {
            System.out.println("\n\tWelcome to the Shop System\n");
            System.out.println("""
                    1. Register a new user
                    2. Login a user
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> {
                    registerUser();
                }
                case 2 -> {
                    if (login()) {
                        System.out.println("Logged in successfully\n");
                        if (currentUser.getRole().equals(Role.ADMIN)) {

                        } else {
                            userMenu();
                        }
                    }
                    System.out.println("Invalid username or password");
                }
            }
        }
    }

    public void registerUser() {
        User newUser = new User();
        System.out.println("Enter username: ");
        newUser.setUserName(scStr.nextLine());
        System.out.println("Enter password: ");
        newUser.setPassword(scStr.nextLine());
        System.out.println("Enter confirm password: ");
        String confirmPassword = scStr.nextLine();
        if (confirmPassword.equals(newUser.getPassword())) {
            userService.add(newUser);
            return;
        }
        System.out.println("Passwords do not match");
    }

    public boolean login() {
        System.out.println("Enter username: ");
        String username = scStr.nextLine();
        System.out.println("Enter password: ");
        String password = scStr.nextLine();
        currentUser = userService.logIn(username, password);
        return currentUser != null;
    }

    public void userMenu() {
        int option = 100;
        while (option != 0) {
            System.out.println(currentUser.getUserName() + " Welcome to the Shop System\n");
            System.out.println("""
                    1. Catalogs
                    2. Bills
                    3. Basket
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> {
                    int i = 1;
                    List<Category> categories = categoryService.findNonParent();
                    for (Category category : categories) {
                        System.out.println(i + ". " + category);
                    }
                    System.out.println("Select category:        0. Exit");
                    i = sc.nextInt();
                    if (i < 1 || i > categories.size()){
                        return;
                    }
                    Category category = categories.get(i);
                    while (!category.isLastCategory()) {
                        categories = categoryService.findAllByParentId(category.getId());
                        for (Category c : categories) {
                            System.out.println(i + ". " + c);
                        }
                        System.out.println("Select category:        0. Exit");
                        i = sc.nextInt();
                        if (i < 1 || i > categories.size()){
                            return;
                        }
                        category = categories.get(i);
                    }
                    i = 1;
                    for (Product p : products) {
                        System.out.println(i + ". " + p.getName());
                    }
                    System.out.println("Enter number of products:      0.  Exit");
                    i = sc.nextInt();
                    if (i > products.size() || i < 1) {
                        return;
                    }
                    Product product = products.get(i - 1);
                    System.out.println("\n\t" + product);
                    System.out.println("Enter amount products:      0.  Exit");
                    i = sc.nextInt();
                    if (i < 1 || i > product.getAmount()) {
                        return;
                    }
                    Basket basket = new Basket();
                    basket.setAmount(i);
                    basket.setProductId(product.getId());
                    basket.setUserId(currentUser.getId());
                    basket.setCreatedBy(currentUser.getId());
                    if (basketService.add(basket)) {
                        System.out.println("Successfully added in basket");
                        product.setAmount(product.getAmount() - i);
                        productService.rewrite();
                    } else {
                        System.out.println("Failed to add in basket");
                    }
                }
                case 2 -> {
                    List<Bill> bills = billService.findByUserId(currentUser.getId());
                    int i = 1;
                    bills.reversed();
                    for (Bill b : bills) System.out.println(i + ". " + b);
                }
                case 3 -> {

                }
            }

        }
    }

    public void adminMenu() {
        int option = 100;
        while (option != 0) {
            System.out.println(currentUser.getUserName() + " Welcome to the Shop admin System\n");
            System.out.println("""
                    1. Catalogs
                    2. Category's
                    3. Products
                    4. Users
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> {

                }
                case 2 -> {

                }
                case 3 -> {

                }
                case 4 -> {

                }
            }
        }
    }
}