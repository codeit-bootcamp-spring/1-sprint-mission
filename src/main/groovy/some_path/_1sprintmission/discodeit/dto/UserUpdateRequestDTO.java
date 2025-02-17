package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus;
import some_path._1sprintmission.discodeit.entiry.enums.UserType;

import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDTO {
    private UUID userId;  // 수정할 대상 유저 ID
    private Optional<String> username = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> email = Optional.empty();
    private Optional<String> phone = Optional.empty();
    private Optional<String> introduce = Optional.empty();
    private Optional<UserType> userType = Optional.empty();
    private Optional<DiscodeStatus> discodeStatus = Optional.empty();
    private Optional<ProfileImageRequestDTO> profileImageRequest = Optional.empty();
}
