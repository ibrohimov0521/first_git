package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.BaseModel;
import uz.shop.model.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductService implements BaseService<Product> {

    ObjectMapper mapper = new ObjectMapper();
    File file = new File("src/main/resources/products.json");
    private List<Product> products = new ArrayList<>();

    public ProductService() {
        readFromFile();
    }

    @Override
    public Product getById(UUID id) {
        readFromFile();
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Product findByName(String name) {
        readFromFile();
        return products.stream()
                .filter(product -> product.isActive() && product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> getAll() {
        readFromFile();
        return products.stream()
                .filter(BaseModel::isActive)
                .collect(Collectors.toList());
    }


    public List<Product> findAllByCategoryId(UUID categoryId) {
        readFromFile();
        return products.stream()
                .filter(p -> p.isActive() && p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());

    }

    @SneakyThrows
    @Override
    public boolean add(Product product) {
        readFromFile();
        boolean exists = products.stream()
                .anyMatch(product1 -> product1.isActive() && product1.getName().equals(product.getName()));
        if (exists) return false;
        products.add(product);
        saveToFile();
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Product product, UUID id) {
        readFromFile();
        Optional<Product> optionalProduct = products.stream()
                .filter(product1 -> product1.isActive() && product1.getId().equals(id))
                .findFirst();
        if (optionalProduct.isPresent()) {
            Product product1 = optionalProduct.get();
            product1.setName(product.getName());
            product1.setPrice(product.getPrice());
            product1.setDescription(product.getDescription());
            product1.setAmount(product.getAmount());
            saveToFile();
            return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean deleteById(UUID id) {
        readFromFile();
        products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .ifPresent(p -> p.setActive(false));
        saveToFile();
        return true;
    }

    @SneakyThrows
    @Override
    public void readFromFile() {
        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        products = mapper.readValue(file, new TypeReference<>() {
        });
    }

    @Override
    public void saveToFile() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
    }
}
