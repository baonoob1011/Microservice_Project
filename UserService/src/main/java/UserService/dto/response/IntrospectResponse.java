package UserService.dto.response;


import UserService.dto.RealmAccess;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectResponse {
    long exp;
    long iat;
    String jti;
    String iss;
    String sub;
    String typ;
    String azp;
    String sid;
    String acr;
    List<String> allowed_origins;
    RealmAccess realm_access;
    String scope;
    boolean email_verified;
    String name;
    String preferred_username;
    String given_name;
    String family_name;
    String email;
    String client_id;
    String username;
    String token_type;
    boolean active;
}
