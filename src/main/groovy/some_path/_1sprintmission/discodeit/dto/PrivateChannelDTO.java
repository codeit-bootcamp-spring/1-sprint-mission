package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.Set;

@Getter
@AllArgsConstructor
public class PrivateChannelDTO {
    private Set<User> users; // 참여할 유저 목록
}
