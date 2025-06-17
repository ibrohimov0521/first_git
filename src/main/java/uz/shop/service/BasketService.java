package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Basket;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasketService implements BaseService<Basket> {
    ObjectMapper mapper = new ObjectMapper();
    private List<Basket> baskets;

    public BasketService() {
        baskets = new ArrayList<>();
        rewrite();
    }

    @Override
    public Basket findById(UUID id) {
        return null;
    }

    @Override
    public List<Basket> findAll() {
        rewrite();
        return baskets;
    }

    public List<Basket> findByUserId(UUID userId) {
        rewrite();
        List<Basket> baskets1 =  new ArrayList<>();
        for (Basket b : this.baskets) {
            if (b.isActive() && b.getUserId().equals(userId)){
                baskets1.add(b);
            }
        }
        return baskets1;
    }

    @SneakyThrows
    @Override
    public boolean add(Basket basket) {
        rewrite();
        for (Basket b : baskets) {
            if (b.getProductId().equals(basket.getProductId()) && b.isActive()) {
                b.setAmount(b.getAmount() + basket.getAmount());
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/baskets.json"), baskets);
                return true;
            }
        }
        baskets.add(basket);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/baskets.json"), baskets);
        return true;
    }

    @Override
    public boolean update(Basket basket, UUID id) {
        return false;
    }

    @SneakyThrows
    public void delete (UUID id) {
        rewrite();
        for (Basket basket : baskets) {
            if (basket.getId().equals(id)) {
                basket.setActive(false);
            }
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/baskets.json"), baskets);
    }

    @SneakyThrows
    @Override
    public void rewrite() {
        File file = new File("src/main/resources/baskets.json");
        if (file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        baskets = mapper.readValue(file, new TypeReference<>() {
        });
    }
}
