package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean isVerified;
    private boolean isOnline;

    public static UserDTO from(User user, boolean isOnline) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getUserEmail().getValue(),
                user.getUserPhone().getValue(),
                user.getIsVerified(),
                isOnline
        );
    }
}
