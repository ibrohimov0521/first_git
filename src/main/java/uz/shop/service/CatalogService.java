package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Catalog;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatalogService implements BaseService<Catalog> {
    ObjectMapper mapper = new ObjectMapper();
    private List<Catalog> catalogs;

    public CatalogService() {
        catalogs = new ArrayList<>();

    }

    @Override
    public Catalog findById(UUID id) {
        rewrite();
        for (Catalog catalog : catalogs) {
            if (catalog.getId().equals(id)) {
                return catalog;
            }
        }
        return null;
    }

    @Override
    public List<Catalog> findAll() {
        rewrite();
        return catalogs;
    }

    @SneakyThrows
    @Override
    public boolean add(Catalog catalog) {
        rewrite();
        for (Catalog t : catalogs) {
            if (t.getName().equals(catalog.getName())) {
                return false;
            }
        }
        catalogs.add(catalog);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/catalogs.json"), catalogs);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Catalog catalog, UUID id) {
        rewrite();
        for (Catalog t : catalogs) {
            if (t.getId().equals(id)) {
                t.setName(catalog.getName());
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/catalogs.json"), catalogs);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void rewrite() {
        File file = new File("src/main/resources/catalogs.json");
        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        catalogs = mapper.readValue(file, new TypeReference<>() {
        });
    }
}
