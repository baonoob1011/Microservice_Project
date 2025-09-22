package ProductService.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStockUpdateEvent {
     Long productId;
     long quantity; // số lượng cần trừ
}
