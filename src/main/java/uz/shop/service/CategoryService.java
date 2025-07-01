package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Category;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryService implements BaseService<Category> {
    File file = new File("src/main/resources/category.json");
    private List<Category> categories;

    public CategoryService() {
        categories = new ArrayList<>();
        categories = FileUtil.read(file, Category.class);
    }

    @Override
    public Category findById(UUID id) {

        return categories.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Category findName(String name) {
        return categories.stream()
                .filter(category -> category.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Category> findNonParent() {
        return categories.stream()
                .filter(c -> c.isActive() && c.getParentId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findAll() {
        return categories;
    }

    public List<Category> findAllByParentId(UUID parentId) {
        return categories.stream().filter(category -> category.getParentId() != null &&
                        category.getParentId().equals(parentId) &&
                        category.isActive())
                .collect(Collectors.toList());

    }

    @SneakyThrows
    @Override
    public boolean add(Category category) {
        boolean isCategory = categories.stream()
                .anyMatch(c -> c.getName().equals(category.getName()));
        if (!isCategory) {
            return false;
        }
        categories.add(category);
        FileUtil.write(file, categories);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Category category, UUID id) {
        Category c = findById(id);
        if (c != null) {
            c.setName(category.getName());
            c.setLastCategory(category.isLastCategory());
            c.setParentId(category.getParentId());
            c.setActive(category.isActive());
            c.setUpdatedDate(category.getCreatedDate());
            FileUtil.write(file, categories);
            return true;
        }

        return false;
    }

}
