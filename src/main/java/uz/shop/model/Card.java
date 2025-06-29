package uz.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Card extends BaseModel {
    private UUID userId;
    private UUID productId;
    private int amount;
    private List<CartItem> items;

    @Data
    @EqualsAndHashCode(callSuper = true)

    public static class CartItem extends BaseModel {
        private UUID productId;
        private int amount;

        public CartItem( UUID productId, int amount) {
            this.productId=productId;
            this.amount=amount;

        }
    }
}
