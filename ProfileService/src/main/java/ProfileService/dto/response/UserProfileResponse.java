package ProfileService.dto.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
