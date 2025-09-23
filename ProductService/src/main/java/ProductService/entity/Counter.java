package ProductService.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "counters")
public class Counter {
    @Id
    String id; // tên sequence, ví dụ "productId"
    long seq;
}
