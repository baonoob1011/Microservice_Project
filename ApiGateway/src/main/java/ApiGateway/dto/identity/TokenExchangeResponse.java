package ApiGateway.dto.identity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenExchangeResponse {
    String accessToken;
    String expiresIn;
    String refreshExpiresIn;
    String refreshToken;
    String tokenType;
    String idToken;
    String scope;
}
