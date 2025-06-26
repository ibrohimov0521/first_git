package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.Bill;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BillService implements BaseService<Bill> {
    File file = new File("src/main/resources/bills.json");
    private List<Bill> bills;

    public BillService() {
        bills = new ArrayList<>();
        bills = FileUtil.read(file, Bill.class);
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
        for (Bill b : bills) {
            if (b != null && b.getId().equals(id) && b.isActive()) {
                b.setAmount(bill.getAmount());
                b.setUserId(bill.getUserId());
                b.setProductId(bill.getProductId());
                b.setUpdatedDate(bill.getCreatedDate());
                FileUtil.write(file,bills);
                return true;
            }
        }
        return false;
    }


}
