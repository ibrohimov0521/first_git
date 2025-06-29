package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.BaseModel;
import uz.shop.model.Card;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CardService implements BaseService<Card> {
    ObjectMapper mapper = new ObjectMapper();
    File path = new File("src/main/resources/baskets.json");
    private List<Card> cards = new ArrayList<>();

    public CardService() {
        readFromFile();
    }

    @Override
    public Card getById(UUID id) {

        return cards.stream()
                .filter(card -> card.isActive() && card.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Card> getAll() {
        readFromFile();
        return cards.stream()
                .filter(BaseModel::isActive)
                .collect(Collectors.toList());
    }

    public List<Card> findByUserId(UUID userId) {
        readFromFile();
        return cards.stream()
                .filter(card -> card.getUserId().equals(userId))
                .findFirst()
                .stream().collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public boolean add(Card basket) {
        readFromFile();
        for (Card card : cards) {
            if (card.getProductId().equals(basket.getProductId()) && card.isActive()) {
                card.setAmount(card.getAmount() + basket.getAmount());
                saveToFile();
                return true;
            }
        }
        cards.add(basket);
        mapper.writerWithDefaultPrettyPrinter().writeValue(path, cards);
        return true;
    }

    @Override
    public boolean update(Card card, UUID id) {
        return false;
    }

    @SneakyThrows
    public void delete(UUID id) {
        readFromFile();
        cards.stream()
                .filter(card -> card.getId().equals(id) && card.isActive())
                .findFirst()
                .ifPresent(card -> card.setActive(false));
        saveToFile();
    }


    @SneakyThrows
    @Override
    public void readFromFile() {
        if (path.length() == 0) {
            Files.writeString(path.toPath(), "[]");
        }
        cards = mapper.readValue(path, new TypeReference<>() {});
    }

    @Override
    public void saveToFile() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(path, cards);
    }

}
