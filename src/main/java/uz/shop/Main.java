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
        menu();
    }

    public static void menu() {
        int option = 100;
        while (option != 0) {
            System.out.println("\n\tWelcome to the Shop System\n");
            System.out.println("""
                    1. Register a new user
                    2. Login a user
                    3. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> registerUser();
                case 2 -> {
                    if (login()) {
                        System.out.println("Logged in successfully\n");
                        if (currentUser.getRole().equals(Role.ADMIN)) {
                            adminMenu();
                        } else {
                            userMenu();
                        }
                    }
                    System.out.println("Invalid username or password");
                }
            }
        }
    }

    public static void registerUser() {
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

    public static boolean login() {
        System.out.println("Enter username: ");
        String username = scStr.nextLine();
        System.out.println("Enter password: ");
        String password = scStr.nextLine();
        currentUser = userService.logIn(username, password);
        return currentUser != null;
    }

    public static void userMenu() {
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
                        System.out.println(i++ + ". " + category);
                    }
                    System.out.println("Select category:        0. Exit");
                    i = sc.nextInt();
                    if (i < 1 || i > categories.size()) {
                        return;
                    }
                    Category category = categories.get(i - 1);
                    while (!category.isLastCategory()) {
                        i = 1;
                        categories = categoryService.findAllByParentId(category.getId());
                        for (Category c : categories) {
                            System.out.println(i++ + ". " + c);
                        }
                        System.out.println("Select category:        0. Exit");
                        i = sc.nextInt();
                        if (i < 1 || i > categories.size()) {
                            category = categoryService.findById(category.getParentId());
                        } else {
                            category = categories.get(i - 1);
                        }
                    }
                    i = 1;
                    List<Product> products = productService.findAllByCategoryId(category.getId());
                    for (Product p : products) {
                        System.out.println(i++ + ". " + p.getName());
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
                    for (Bill b : bills) System.out.println(i++ + ". " + b);
                }
                case 3 -> basketMenu();
            }
        }
    }

    public static void adminMenu() {
        int option = 100;
        while (option != 0) {
            System.out.println(currentUser.getUserName() + " Welcome to the Shop admin System\n");
            System.out.println("""
                    1. Category's
                    2. Products
                    3. Users
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> categoryManu();
                case 2 -> productManu();
                case 3 -> {
                    List<User> users = userService.findAll();
                    for (User user : users) {
                        System.out.println(user);
                    }
                }
            }
        }
    }

    public static void categoryManu() {
        int option = 100;
        while (option != 0) {
            System.out.println("""
                    1. List categories
                    2. add categories
                    3. delete categories
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> {
                    int i = 1;
                    List<Category> categories = categoryService.findNonParent();
                    for (Category c : categories) {
                        System.out.println(i++ + ". " + c);
                    }
                    System.out.println("Select category:        0. Exit");
                    i = sc.nextInt();
                    if (i < 1 || i > categories.size()) {
                        return;
                    }
                    Category category = categories.get(i - 1);
                    while (!category.isLastCategory()) {
                        categories = categoryService.findAllByParentId(category.getId());
                        i = 1;
                        for (Category c : categories) {
                            System.out.println(i++ + ". " + c);
                        }
                        System.out.println("Select category:        0. Exit");
                        i = sc.nextInt();
                        if (i < 1 || i > categories.size()) {
                            category = categoryService.findById(category.getParentId());
                        } else {
                            category = categories.get(i - 1);
                        }
                    }
                    i = 1;
                    List<Product> products = productService.findAllByCategoryId(category.getId());
                    for (Product p : products) {
                        System.out.println(i++ + ". " + p.getName());
                    }
                    System.out.println("0. Exit");
                    sc.nextLine();
                }
                case 2 -> {
                    Category newCategory = new Category();
                    newCategory.setCreatedBy(currentUser.getId());
                    System.out.println("Enter Category Name: ");
                    newCategory.setName(scStr.nextLine());
                    System.out.println("""
                            1. Is it last Category
                            2. Is it Catalog
                            0. no
                            """);
                    switch (sc.nextInt()) {
                        case 1 -> {
                            System.out.println("Enter Name Parent Category");
                            Category parCategory = categoryService.findName(scStr.nextLine());
                            newCategory.setParentId(parCategory.getId());
                            newCategory.setLastCategory(true);
                        }
                        case 2 -> {
                        }
                        case 0 -> {
                            System.out.println("Enter Name Parent Category");
                            Category parCategory = categoryService.findName(scStr.nextLine());
                            newCategory.setParentId(parCategory.getId());
                        }
                    }
                    categoryService.add(newCategory);
                }
                case 3 -> {
                    System.out.println("Enter category name: ");
                    Category category = categoryService.findName(scStr.nextLine());
                    category.setActive(false);
                    if (categoryService.update(category, category.getId())){
                        System.out.println("Deleted!!");
                    } else {
                        System.out.println("Error!!");
                    }
                }
            }
        }
    }

    public static void productManu() {
        int option = 100;
        while (option != 0) {
            System.out.println("""
                    1. All product list
                    2. Add product
                    3. Delete product
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> {
                    List<Product> products = productService.findAll();
                    for (Product product : products) {
                        System.out.println(product);
                    }
                }
                case 2 -> {
                    int i = 1;
                    List<Category> categories = categoryService.findNonParent();
                    for (Category category : categories) {
                        System.out.println(i++ + ". " + category);
                    }
                    System.out.println("Select category:        0. Exit");
                    i = sc.nextInt();
                    if (i < 1 || i > categories.size()) {
                        return;
                    }
                    Category category = categories.get(i - 1);
                    while (!category.isLastCategory()) {
                        categories = categoryService.findAllByParentId(category.getId());
                        i = 1;
                        for (Category c : categories) {
                            System.out.println(i++ + ". " + c);
                        }
                        System.out.println("Select category:        0. Exit");
                        i = sc.nextInt();
                        if (i < 1 || i > categories.size()) {
                            category = categoryService.findById(category.getParentId());
                        } else {
                            category = categories.get(i - 1);
                        }
                    }
                    Product newProduct = new Product();
                    System.out.println("Enter product Name: ");
                    newProduct.setName(scStr.nextLine());
                    System.out.println("Enter product Price");
                    newProduct.setPrice(sc.nextDouble());
                    System.out.println("Enter amount: ");
                    newProduct.setAmount(sc.nextInt());
                    System.out.println("Enter Description: ");
                    newProduct.setDescription(scStr.nextLine());
                    newProduct.setCreatedBy(currentUser.getId());
                    newProduct.setCategoryId(category.getId());
                    if (productService.add(newProduct)) {
                        System.out.println("Successfully added!!!");
                    } else {
                        System.out.println("ERROR!");
                    }
                }
                case 3 -> {
                    System.out.println("Enter product name: ");
                    Product product = productService.findByName(scStr.nextLine());
                    if (productService.deleteById(product.getId())) {
                        System.out.println("Deleted!!");
                    }
                }
            }
        }
    }

    public static void basketMenu() {
        int option = 100;
        while (option != 0) {
            List<Basket> baskets = basketService.findByUserId(currentUser.getId());
            int i=1;
            double price =0;
            for (Basket basket : baskets) {
                if (basket.isActive()) {
                    price += productService.findById(basket.getProductId()).getPrice();
                    System.out.println(i++ + ". " + basket);
                }
            }
            System.out.println("Umumiy summa: " + price + """
                    \n
                    1. Xarid qilish
                    2. Hammasini o'chirish
                    0. Exit
                    """);
            option = sc.nextInt();
            switch (option) {
                case 1 -> {
                    for (Basket basket : baskets) {
                        Bill bill = new Bill();
                        bill.setAmount(basket.getAmount());
                        bill.setProductId(basket.getProductId());
                        bill.setUserId(basket.getUserId());
                        billService.add(bill);
                        basketService.delete(basket.getId());
                    }
                }
                case 2 -> {
                    for (Basket basket : baskets) {
                        basketService.delete(basket.getId());
                    }
                }
            }
        }
    }
}