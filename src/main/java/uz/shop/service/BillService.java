package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.Bill;
import uz.shop.util.FileUtil;

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
    }

    @Override
    public Bill findById(UUID id) {
        for (Bill b : bills) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Bill> findAll() {
        return bills;
    }

    public List<Bill> findByUserId(UUID userId) {

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

        bills.add(bill);
        FileUtil.write(file,bills);
        return true;
    }

    @Override
    public boolean update(Bill bill, UUID id) {
        return false;
    }


}
