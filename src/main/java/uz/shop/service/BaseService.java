package uz.shop.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BaseService <T> {
    T getById(UUID id);
    List<T> getAll();
    boolean add(T t);
    boolean update(T t, UUID id);
    void readFromFile();
    void saveToFile() throws IOException;
}
