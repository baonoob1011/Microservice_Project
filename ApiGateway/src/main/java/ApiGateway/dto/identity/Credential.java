package ApiGateway.dto.identity;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Credential {

    String type;
    String value;
    Boolean temporary;
}
