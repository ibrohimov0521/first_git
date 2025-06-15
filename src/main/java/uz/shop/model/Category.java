package uz.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Category extends Base {
    private String name;
    private UUID catalogId;
}
