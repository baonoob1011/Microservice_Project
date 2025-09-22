package UserService.dto.identity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeTokenParam {
    String grant_type;
    String client_id;
    String client_secret;
    String scope;
    //login
    String username;
    String password;
    //introspect
    String token;
    //logout
    String token_type_hint;

}
