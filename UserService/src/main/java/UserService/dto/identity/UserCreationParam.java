package UserService.dto.identity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationParam {
    String username;          // Tên đăng nhập
    String email;             // Email
    Boolean enabled;          // Có active không
    String firstName;         // Tên
    String lastName;          // Họ
    Boolean emailVerified;    // Email đã verify chưa
    List<Credential> credentials; // Danh sách credentials
}
