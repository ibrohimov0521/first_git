package uz.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Product extends Base {
    private String name;
    private UUID categoryId;
    private double price;
    private int amount;
    private String description;
}
