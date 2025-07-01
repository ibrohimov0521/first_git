package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Basket;
import uz.shop.model.Bill;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasketService implements BaseService<Basket> {
    File file = new File("src/main/resources/baskets.json");
    private List<Basket> baskets;

    public BasketService() {
        baskets = new ArrayList<>();
        baskets = FileUtil.read(file, Basket.class);
    }

    @Override
    public Basket findById(UUID id) {
        return baskets.stream()
                .filter(basket -> basket.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Basket> findAll() {
        return baskets;
    }

    public List<Basket> findByUserId(UUID userId) {
        return baskets.stream()
                .filter(basket -> basket.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public boolean add(Basket basket) {

        boolean isBasket = baskets.stream()
                .anyMatch(b -> b.getProductId().equals(basket.getProductId()) && b.isActive());

        if (!isBasket) {
            return false;
        }
        baskets.add(basket);
        FileUtil.write(file, baskets);
        return true;
    }

    @Override
    public boolean update(Basket basket, UUID id) {
        Basket b = findById(id);
        if (b != null) {
            b.setAmount(basket.getAmount());
            b.setUserId(basket.getUserId());
            b.setProductId(basket.getProductId());
            b.setUpdatedDate(basket.getCreatedDate());
            FileUtil.write(file, baskets);
            return true;
        }

        return false;
    }

    @SneakyThrows
    public void delete(UUID id) {
        Basket basket = findById(id);
        basket.setActive(false);
        FileUtil.write(file, baskets);
    }

}
