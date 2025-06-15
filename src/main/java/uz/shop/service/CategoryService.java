package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Category;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryService implements BaseService<Category> {
    ObjectMapper mapper = new ObjectMapper();
    private List<Category> categories;

    public CategoryService() {
        categories = new ArrayList<>();
        rewrite();
    }

    @Override
    public Category findById(UUID id) {
        rewrite();
        for (Category c : categories) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Category findName(String name) {
        rewrite();
        for (Category c : categories) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public List<Category> findNonParent() {
        rewrite();
        List<Category> categories1 = new ArrayList<>();
        for (Category c : categories) {
            if (c.getParentId() == null) {
                categories1.add(c);
            }
        }
        return categories1;
    }

    @Override
    public List<Category> findAll() {
        rewrite();
        return categories;
    }

    public List<Category> findAllByParentId(UUID parentId) {
        rewrite();
        List<Category> categories = new ArrayList<>();
        for (Category c : this.categories) {
            if (parentId != null && parentId.equals(c.getParentId())) {
                categories.add(c);
            }
        }
        return categories;
    }



    @SneakyThrows
    @Override
    public boolean add(Category category) {
        rewrite();
        for (Category c : categories) {
            if (c.getName().equals(category.getName())) {
                return false;
            }
        }
        categories.add(category);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/category.json"), categories);
        return true;
    }

    @Override
    public boolean update(Category category, UUID id) {
        return false;
    }

    @SneakyThrows
    @Override
    public void rewrite() {
        File file = new File("src/main/resources/category.json");
        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        categories = mapper.readValue(file, new TypeReference<>() {
        });
    }
}
