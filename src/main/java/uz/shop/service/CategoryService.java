package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Category;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryService implements BaseService<Category> {
    File file = new File("src/main/resources/category.json");
    private final List<Category> categories;

    public CategoryService() {
        categories = new ArrayList<>();
    }

    @Override
    public Category findById(UUID id) {
        for (Category c : categories) {
            if (c.isActive() && c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Category findName(String name) {
        for (Category c : categories) {
            if (c.isActive() && c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public List<Category> findNonParent() {
        List<Category> categories1 = new ArrayList<>();
        for (Category c : categories) {
            if (c.isActive() && c.getParentId() == null) {
                categories1.add(c);
            }
        }
        return categories1;
    }

    @Override
    public List<Category> findAll() {
        return categories;
    }

    public List<Category> findAllByParentId(UUID parentId) {

        List<Category> categories = new ArrayList<>();
        for (Category c : this.categories) {
            if ( c.isActive() && parentId != null && parentId.equals(c.getParentId())) {
                categories.add(c);
            }
        }
        return categories;
    }

    @SneakyThrows
    @Override
    public boolean add(Category category) {
        for (Category c : categories) {
            if (c.getName().equals(category.getName())) {
                return false;
            }
        }

        categories.add(category);
        FileUtil.write(file,categories);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Category category, UUID id) {
        for (Category c : categories) {
            if (c.isActive() && c.getId().equals(id)){
                c.setName(category.getName());
                c.setLastCategory(category.isLastCategory());
                c.setParentId(category.getParentId());
                c.setActive(category.isActive());
                FileUtil.write(file,categories);
                return true;
            }
        }
        return false;
    }

}
