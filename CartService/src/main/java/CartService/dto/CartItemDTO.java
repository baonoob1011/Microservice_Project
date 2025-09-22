package CartService.dto;

import CartService.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private String id;       // MongoDB ObjectId
    private String userId;   // User sở hữu giỏ hàng
    private List<CartItem> items;
}
