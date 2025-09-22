package CartService.dto.response;

import CartService.entity.CartItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    String id;       // MongoDB ObjectId
    String userId;   // User sở hữu giỏ hàng
    List<CartItem> items;
}
