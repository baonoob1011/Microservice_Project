package UserService.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    String username;
    String password;
    String email;
    String lastName;
    String firstName;
    String address;
    String phone;
    LocalDate dateOfBirth;
}
