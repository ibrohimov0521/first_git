package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Bill;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BillService implements BaseService<Bill> {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File("src/main/resources/bills.json");
    private List<Bill> bills;

    public BillService() {
        bills = new ArrayList<>();
        readFromFile();
    }

    @Override
    public Bill getById(UUID id) {
        return bills.stream()
                .filter(bill -> bill.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Bill> getAll() {
        readFromFile();
        return bills;
    }

    public List<Bill> findByUserId(UUID userId) {
        readFromFile();
        return bills.stream()
                .filter(bill -> bill.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public boolean add(Bill bill) {
        readFromFile();
        bills.add(bill);
        saveToFile();
        return true;
    }

    @Override
    public boolean update(Bill bill, UUID id) {
        return false;
    }

    @SneakyThrows
    @Override
    public void readFromFile() {
        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        bills = mapper.readValue(file, new TypeReference<>() {
        });
    }

    @Override
    public void saveToFile() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file,bills
        );
    }
}
