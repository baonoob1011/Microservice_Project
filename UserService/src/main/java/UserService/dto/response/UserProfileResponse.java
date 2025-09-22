package UserService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse implements Serializable {
    String profileId;
    String userId;
    String username;
    String address;
    String phone;
    LocalDate dateOfBirth;
}
