package UserService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationResponse {
    String userId;
    String username;
    String email;
    String lastName;
    String firstName;
}
