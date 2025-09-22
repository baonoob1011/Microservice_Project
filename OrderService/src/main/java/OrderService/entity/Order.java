package OrderService.entity;

import OrderService.dto.CartItem;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    String id;
    String userId;
    String status;
    Double totalAmount;
    @CreatedDate
    LocalDateTime createdAt;
    List<CartItem> items;
}