package uz.shop.service;

import java.util.List;
import java.util.UUID;

public interface BaseService <T> {
    T findById(UUID id);
    List<T> findAll();
    boolean add(T t);
    boolean update(T t, UUID id);
}
