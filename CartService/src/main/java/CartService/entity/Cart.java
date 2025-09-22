package CartService.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;       // MongoDB ObjectId
    private String userId;   // User sở hữu giỏ hàng
    private List<CartItem> items;
}
