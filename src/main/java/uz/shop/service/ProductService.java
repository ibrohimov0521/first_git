package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Product;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductService implements BaseService<Product> {

    ObjectMapper mapper = new ObjectMapper();
    File file = new File("src/main/resources/products.json");
    private List<Product> products;

    public ProductService() {
        products = new ArrayList<>();
        rewrite();
    }

    @Override
    public Product findById(UUID id) {
        rewrite();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public Product findByName(String name) {
        rewrite();
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        rewrite();
        List<Product> products1 = new ArrayList<>();
        for (Product product : products) {
            if (product.isActive()){
                products1.add(product);
            }
        }
        return products1;
    }

    public List<Product> findAllByCategoryId(UUID categoryId) {
        rewrite();
        List<Product> products = new ArrayList<>();
        for (Product p : this.products) {
            if (p.getCategoryId().equals(categoryId) && p.isActive()) {
                products.add(p);
            }
        }
        return products;
    }

    @SneakyThrows
    @Override
    public boolean add(Product product) {
        rewrite();
        for (Product product1 : products) {
            if (product1.getName().equals(product.getName()) && product1.isActive()) {
                return false;
            }
        }
        products.add(product);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Product product, UUID id) {
        rewrite();
        for (Product product1 : products) {
            if (product1.getId().equals(id) && product1.isActive()) {
                product1.setName(product.getName());
                product1.setPrice(product.getPrice());
                product1.setDescription(product.getDescription());
                product1.setAmount(product.getAmount());
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public boolean deleteById (UUID id) {
        rewrite();
        for (Product product : products) {
            if (product.getId().equals(id)){
                product.setActive(false);
            }
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        return true;
    }

    @SneakyThrows
    @Override
    public void rewrite() {
        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        products = mapper.readValue(file, new TypeReference<>() {
        });
    }
}
