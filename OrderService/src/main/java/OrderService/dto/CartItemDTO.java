package OrderService.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDTO {
     String id;       // MongoDB ObjectId
     String userId;   // User sở hữu giỏ hàng
     List<CartItem> items;
}
