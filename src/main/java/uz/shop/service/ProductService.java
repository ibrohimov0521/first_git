package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Product;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductService implements BaseService<Product> {
    File file = new File("src/main/resources/products.json");
    private List<Product> products;

    public ProductService() {
        products = new ArrayList<>();
        products = FileUtil.read(file, Product.class);

    }

    @Override
    public Product findById(UUID id) {

        Product p = products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
        return null;
    }

    public Product findByName(String name) {

        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return products.stream()
                .filter(p -> p.isActive())
                .collect(Collectors.toList());
    }

    public List<Product> findAllByCategoryId(UUID categoryId) {

        return products.stream()
                .filter(p -> p.getCategoryId().equals(categoryId) && p.isActive())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public boolean add(Product product) {

        boolean isProduct = products.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));

        if (!isProduct) {
            return false;
        }

        products.add(product);
        FileUtil.write(file, products);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Product product, UUID id) {
        Product product1 = findById(id);

        if (product1 != null) {
            product1.setName(product.getName());
            product1.setPrice(product.getPrice());
            product1.setDescription(product.getDescription());
            product1.setAmount(product.getAmount());
            product1.setUpdatedDate(product.getCreatedDate());
            FileUtil.write(file, products);
            return true;
        }

        return false;
    }

    @SneakyThrows
    public boolean deleteById(UUID id) {
        Product product = findById(id);
        product.setActive(false);
        FileUtil.write(file, products);
        return true;
    }

}
