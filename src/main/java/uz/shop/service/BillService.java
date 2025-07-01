package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Bill;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BillService implements BaseService<Bill> {
    File file = new File("src/main/resources/bills.json");
    private List<Bill> bills;

    public BillService() {
        bills = new ArrayList<>();
        bills = FileUtil.read(file, Bill.class);
    }

    @Override
    public Bill findById(UUID id) {
        return bills.stream()
                .filter(bill -> bill.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Bill> findAll() {
        return bills;
    }

    public List<Bill> findByUserId(UUID userId) {

        return bills.stream()
                .filter(bill -> bill.getUserId().equals(userId) && bill.isActive())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public boolean add(Bill bill) {

        bills.add(bill);
        FileUtil.write(file, bills);
        return true;
    }

    @Override
    public boolean update(Bill bill, UUID id) {
        Bill b = findById(id);
        if (b != null) {
            b.setAmount(bill.getAmount());
            b.setUserId(bill.getUserId());
            b.setProductId(bill.getProductId());
            b.setUpdatedDate(bill.getCreatedDate());
            FileUtil.write(file, bills);
            return true;
        }

        return false;
    }


}
