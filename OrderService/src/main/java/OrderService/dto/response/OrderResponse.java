package OrderService.dto.response;

import OrderService.dto.CartItem;
import OrderService.entity.OrderItem;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String orderId;
    String userId;
    Double totalAmount;
    LocalDateTime createdAt;
    List<CartItem> items;
}