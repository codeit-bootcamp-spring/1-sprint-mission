package some_path._1sprintmission.discodeit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private ProfileImageRequestDTO profileImageRequest;
}