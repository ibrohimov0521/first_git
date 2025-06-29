package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Category;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryService implements BaseService<Category> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File path = new File("src/main/resources/category.json");
    private List<Category> categories = new ArrayList<>();

    public CategoryService() {
        readFromFile();
    }

    @Override
    public Category getById(UUID id) {
        readFromFile();
        return categories.stream()
                .filter(c -> c.isActive() && c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Category getName(String name) {
        readFromFile();
        return categories.stream()
                .filter(c -> c.isActive() && c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Category> findNonParent() {
        readFromFile();
        return categories.stream()
                .filter(c -> c.isActive() && c.getParentId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getAll() {
        readFromFile();
        return categories;
    }

    public List<Category> findAllByParentId(UUID parentId) {
        readFromFile();
        return categories.stream()
                .filter(c -> c.isActive() && Objects.equals(c.getParentId(), parentId))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public boolean add(Category category) {
        readFromFile();
        boolean exists = categories.stream()
                .anyMatch(c -> c.getName().equals(category.getName()));
        if (exists) return false;

        categories.add(category);
        saveToFile();
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Category category, UUID id) {
        readFromFile();
        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.isActive() && c.getId().equals(id))
                .findFirst();

        if (optionalCategory.isPresent()) {
            Category c = optionalCategory.get();
            c.setName(category.getName());
            c.setLastCategory(category.isLastCategory());
            c.setParentId(category.getParentId());
            c.setActive(category.isActive());
            saveToFile();
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void readFromFile() {
        if (!path.exists() || path.length() == 0) {
            Files.writeString(path.toPath(), "[]");
        }
        categories = mapper.readValue(path, new TypeReference<List<Category>>() {});
    }

    @SneakyThrows
    @Override
    public void saveToFile() {
        mapper.writerWithDefaultPrettyPrinter().writeValue(path, categories);
    }
}
