package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Bill;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BillService implements BaseService<Bill> {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File("src/main/resources/bills.json");
    private List<Bill> bills;

    public BillService() {
        bills = new ArrayList<>();
        rewrite();
    }

    @Override
    public Bill findById(UUID id) {
        rewrite();
        for (Bill b : bills) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Bill> findAll() {
        rewrite();
        return bills;
    }

    public List<Bill> findByUserId(UUID userId) {
        rewrite();
        List<Bill> bills = new ArrayList<>();
        for (Bill b : this.bills) {
            if (b.getUserId().equals(userId) && b.isActive()) {
                bills.add(b);
            }
        }
        return bills;
    }

    @SneakyThrows
    @Override
    public boolean add(Bill bill) {
        rewrite();
        bills.add(bill);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, bills);
        return true;
    }

    @Override
    public boolean update(Bill bill, UUID id) {
        return false;
    }

    @SneakyThrows
    @Override
    public void rewrite() {
        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
        bills = mapper.readValue(file, new TypeReference<>() {
        });
    }
}
