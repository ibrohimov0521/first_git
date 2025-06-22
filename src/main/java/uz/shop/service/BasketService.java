package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Basket;
import uz.shop.model.Bill;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasketService implements BaseService<Basket> {
    File file = new File("src/main/resources/baskets.json");
    private List<Basket> baskets;

    public BasketService() {
        baskets = new ArrayList<>();
        baskets = FileUtil.read(file, Basket.class);
    }

    @Override
    public Basket findById(UUID id) {
        return null;
    }

    @Override
    public List<Basket> findAll() {
        return baskets;
    }

    public List<Basket> findByUserId(UUID userId) {
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
        for (Basket b : baskets) {
            if (b.getProductId().equals(basket.getProductId()) && b.isActive()) {
                b.setAmount(b.getAmount() + basket.getAmount());
                FileUtil.write(file,baskets);
                return true;
            }
        }
        baskets.add(basket);
        FileUtil.write(file,baskets);
        return true;
    }

    @Override
    public boolean update(Basket basket, UUID id) {
        for (Basket b : baskets) {
            if (b != null && b.getId().equals(id) && b.isActive()) {
                b.setAmount(basket.getAmount());
                b.setUserId(basket.getUserId());
                b.setProductId(basket.getProductId());
                FileUtil.write(file,baskets);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public void delete (UUID id) {
        for (Basket basket : baskets) {
            if (basket.getId().equals(id)) {
                basket.setActive(false);
            }
        }
        FileUtil.write(file,baskets);
    }

}
