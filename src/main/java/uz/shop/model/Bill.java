package uz.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bill extends BaseModel {
    private UUID userId;
    private UUID productId;
    private int amount;
}
